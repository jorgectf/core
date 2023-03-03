package com.dotcms.junit;

import com.dotcms.IntegrationTestBase;
import com.dotcms.datagen.TestDataUtils;
import com.dotmarketing.business.APILocator;
import com.dotmarketing.common.reindex.ReindexQueueAPI;
import com.dotmarketing.common.reindex.ReindexThread;
import com.dotmarketing.db.DbConnectionFactory;
import com.dotmarketing.db.HibernateUtil;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotHibernateException;
import com.dotmarketing.util.Config;
import com.dotmarketing.util.Logger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import static com.dotmarketing.util.Config.USE_CONFIG_TEST_OVERRIDE_TRACKER;

public class RuleWatcher extends TestWatcher {

    public static final String USE_TEST_INDEXER_TRACKER = "USE_TEST_INDEXER_TRACKER";
    public static final String USE_TEST_TRANSACTION_TRACKER = "USE_TEST_TRANSACTION_TRACKER";
    public static final String TEST_TRANSACTION_TRACKER_AUTO_CLOSE_TYPE = "TEST_TRANSACTION_TRACKER_AUTO_CLOSE_TYPE";
    public static final String TRANSACTION_AUTO_CLOSE_DEFAULT = "rollback";
    private Map<Integer, Map<String, String>> overrides = new ConcurrentHashMap<>();

    @Override
    protected void starting(Description description) {
        Logger.info(RuleWatcher.class, ">>>>>>>>>>>>>>>>>>>>>>>>>>");
        Logger.info(RuleWatcher.class,
                String.format(">>> %s - %s",
                        description,
                        "starting..."));
        Logger.info(RuleWatcher.class, ">>>>>>>>>>>>>>>>>>>>>>>>>>");
        configOverrideTrackerBefore(description);

    }

    @Override
    protected void finished(Description description) {
        Logger.info(RuleWatcher.class, ">>>>>>>>>cleanup>>>>>>>>>>");
        transactionTrackerAfter(description);
        configOverrideTrackerAfter(description);
        indexerTrackerAfter();
        Logger.info(RuleWatcher.class,
                String.format(">>> %s - %s",
                        description,
                        "finished..."));
    }
    //TODO: these should be moved to different classes they could be handled as their own Rules

    private static void indexerTrackerAfter() {
        if (Config.getBooleanProperty(USE_TEST_INDEXER_TRACKER, false)) {
            Logger.info(MainBaseSuite.class, "Checking indexer status...");
            try {
                ReindexQueueAPI queueAPI = APILocator.getReindexQueueAPI();
                if (queueAPI.areRecordsLeftToIndex()) {
                    Logger.info(MainBaseSuite.class,
                            "Indexer is not empty, waiting for it to finish");
                    if (!ReindexThread.isWorking()) {
                        Logger.info(MainBaseSuite.class, "Indexer was not running, unpausing now");
                        ReindexThread.unpause();
                    }
                    TestDataUtils.waitForEmptyQueue();
                    boolean queueEmpty = TestDataUtils.waitForEmptyQueue();
                    Logger.info(MainBaseSuite.class, "Indexer Complete=" + queueEmpty);
                }
            } catch (DotDataException e) {
                throw new RuntimeException("Error accessing Index", e);
            }
        }
    }


    private void configOverrideTrackerBefore(Description description) {
        if (Config.getBooleanProperty(USE_CONFIG_TEST_OVERRIDE_TRACKER, false)) {
            if (Logger.isDebugEnabled(RuleWatcher.class)) {
                Config.getOverrides().forEach((k, v) -> Logger.warn(this.getClass(),
                        () -> "Config overrides before test: " + k + " = " + v));
            }
            overrides.put(description.hashCode(), Config.getOverrides());
        }
    }

    private void configOverrideTrackerAfter(Description description) {
        if (Config.getBooleanProperty(USE_CONFIG_TEST_OVERRIDE_TRACKER, false)) {
            Logger.warn(RuleWatcher.class, "Checking for modified Config overrides...");
            Map<String, String> modifiedOverrides = Config.compareOverrides(
                    overrides.get(description.hashCode()));
            overrides.remove(description.hashCode());
            if (!modifiedOverrides.isEmpty()) {
                String mapContents = modifiedOverrides.entrySet().stream()
                        .map(entry -> entry.getKey() + "=" + entry.getValue())
                        .reduce((a, b) -> a + ", " + b).orElse("empty");
                Logger.warn(IntegrationTestBase.class,
                        "Modified Config overrides after:" + mapContents);
            }
        }
    }

    private static void transactionTrackerAfter(Description description) {
        if (Config.getBooleanProperty(USE_TEST_TRANSACTION_TRACKER, false)) {
            if (DbConnectionFactory.inTransaction()) {
                Logger.error(RuleWatcher.class,
                        "Test " + description + " has open transaction after");
                Object responseAction = Config.getStringProperty(
                        TEST_TRANSACTION_TRACKER_AUTO_CLOSE_TYPE, TRANSACTION_AUTO_CLOSE_DEFAULT);
                try {
                    if (Config.getStringProperty(TEST_TRANSACTION_TRACKER_AUTO_CLOSE_TYPE,
                            TRANSACTION_AUTO_CLOSE_DEFAULT).equals("commit")) {
                        HibernateUtil.commitTransaction();
                    } else {
                        HibernateUtil.rollbackTransaction();
                    }
                } catch (DotHibernateException e) {
                    Logger.error(RuleWatcher.class,
                            "Error force " + responseAction + " back transaction");
                }
            }

            if (DbConnectionFactory.connectionExists()) {
                Logger.warn(RuleWatcher.class,
                        "Test " + description + " has open connection after");
                DbConnectionFactory.closeSilently();
            }

        }
    }

}