package com.dotmarketing.startup.runonce;

import com.dotcms.business.WrapInTransaction;
import com.dotmarketing.common.db.DotConnect;
import com.dotmarketing.exception.DotDataException;
import com.dotmarketing.exception.DotRuntimeException;
import com.dotmarketing.startup.StartupTask;

import java.util.List;

/**
 * Adds Hash Indices to the following database tables related to Workflows:
 * <ul>
 *     <li>{@code workflow_comment}</li>
 *     <li>{@code workflow_history}</li>
 *     <li>{@code workflowtask_files}</li>
 * </ul>
 * When coparing Hash Indices versus B-Tree Indices in Postgres, they are 20-60% faster for reads that are doing lookups
 * using {@code =}. Additionally, they're usually around half the size compared to B-Tree Indices, which means faster
 * lookups.
 *
 * @author Jose Castro
 * @since Jun 22nd, 2023
 */
public class Task230522AddHashIndicesToWorkflowTables implements StartupTask {

    private static final String CREATE_HASH_INDEX_SQL = "CREATE INDEX IF NOT EXISTS %s_hash_idx ON %s USING HASH(workflowtask_id)";
    private static final List<String> tables = List.of("workflow_comment", "workflow_history", "workflowtask_files");

    @Override
    public boolean forceRun() {
        return Boolean.TRUE;
    }

    @WrapInTransaction
    @Override
    public void executeUpgrade() throws DotDataException, DotRuntimeException {
        final DotConnect dc = new DotConnect();
        tables.forEach(table -> {
            try {
                dc.setSQL(String.format(CREATE_HASH_INDEX_SQL, table, table));
                dc.loadResult();
            } catch (final DotDataException e) {
                throw new DotRuntimeException(String.format("Failed to create Hash Index on table '%s'", table), e);
            }
        });
    }

}
