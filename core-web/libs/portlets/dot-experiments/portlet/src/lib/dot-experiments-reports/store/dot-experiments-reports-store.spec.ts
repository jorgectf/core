import {
    createServiceFactory,
    mockProvider,
    SpectatorService,
    SpyObject
} from '@ngneat/spectator/jest';
import { of } from 'rxjs';

import { Title } from '@angular/platform-browser';
import { ActivatedRoute } from '@angular/router';

import { MessageService } from 'primeng/api';

import { DotMessageService } from '@dotcms/data-access';
import {
    BayesianStatusResponse,
    ComponentStatus,
    DotExperimentStatusList,
    DotExperimentVariantDetail,
    ReportSummaryLegendByBayesianStatus
} from '@dotcms/dotcms-models';
import { DotExperimentsService } from '@dotcms/portlets/dot-experiments/data-access';
import {
    getExperimentMock,
    getExperimentResultsMock,
    MockDotMessageService
} from '@dotcms/utils-testing';
import { DotHttpErrorManagerService } from '@services/dot-http-error-manager/dot-http-error-manager.service';

import {
    DotExperimentsReportsState,
    DotExperimentsReportsStore
} from './dot-experiments-reports-store';

const EXPERIMENT_MOCK = getExperimentMock(1);
const EXPERIMENT_MOCK_RESULTS = getExperimentResultsMock(0);

const ActivatedRouteMock = {
    snapshot: {
        params: {
            experimentId: EXPERIMENT_MOCK.id,
            pageId: EXPERIMENT_MOCK.pageId
        }
    }
};

const messageServiceMock = new MockDotMessageService({
    Sunday: 'Sunday',
    Monday: 'Monday',
    Tuesday: 'Tuesday',
    Wednesday: 'Wednesday',
    Thursday: 'Thursday',
    Friday: 'Friday',
    Saturday: 'Saturday'
});

describe('DotExperimentsReportsStore', () => {
    let spectator: SpectatorService<DotExperimentsReportsStore>;
    let store: DotExperimentsReportsStore;
    let dotExperimentsService: SpyObject<DotExperimentsService>;

    const createStoreService = createServiceFactory({
        service: DotExperimentsReportsStore,
        providers: [
            mockProvider(Title),
            {
                provide: ActivatedRoute,
                useValue: ActivatedRouteMock
            },
            {
                provide: DotMessageService,
                useValue: messageServiceMock
            },
            mockProvider(MessageService),
            mockProvider(DotExperimentsService),
            mockProvider(DotHttpErrorManagerService)
        ]
    });

    beforeEach(() => {
        spectator = createStoreService({});

        store = spectator.inject(DotExperimentsReportsStore);
        dotExperimentsService = spectator.inject(DotExperimentsService);
        dotExperimentsService.getById.mockReturnValue(of(EXPERIMENT_MOCK));
        dotExperimentsService.getResults.mockReturnValue(of(EXPERIMENT_MOCK_RESULTS));
    });

    it('should set initial data', (done) => {
        spectator.service.loadExperimentAndResults(EXPERIMENT_MOCK.id);

        const expectedInitialState: DotExperimentsReportsState = {
            experiment: EXPERIMENT_MOCK,
            status: ComponentStatus.IDLE,
            results: EXPERIMENT_MOCK_RESULTS
        };

        expect(dotExperimentsService.getById).toHaveBeenCalledWith(EXPERIMENT_MOCK.id);
        store.state$.subscribe((state) => {
            expect(state).toEqual(expectedInitialState);
            done();
        });
    });

    it('should have isLoading$ from the store', (done) => {
        spectator.service.loadExperimentAndResults(EXPERIMENT_MOCK.id);
        store.isLoading$.subscribe((data) => {
            expect(data).toEqual(false);
            done();
        });
    });

    it('should update component status to the store', (done) => {
        store.setComponentStatus(ComponentStatus.LOADED);
        store.state$.subscribe(({ status }) => {
            expect(status).toBe(ComponentStatus.LOADED);
            done();
        });
    });

    it('should get FALSE from showExperimentSummary$ if Experiment status is different of Running', (done) => {
        dotExperimentsService.getById.mockReturnValue(of(EXPERIMENT_MOCK));
        spectator.service.loadExperimentAndResults(EXPERIMENT_MOCK.id);

        store.showExperimentSummary$.subscribe((value) => {
            expect(value).toEqual(false);
            done();
        });
    });

    it('should get TRUE from showExperimentSummary$ if Experiment status is different of Running', (done) => {
        dotExperimentsService.getById.mockReturnValue(
            of({
                ...EXPERIMENT_MOCK,
                status: DotExperimentStatusList.RUNNING
            })
        );
        dotExperimentsService.getResults.mockReturnValue(of(EXPERIMENT_MOCK_RESULTS));

        spectator.service.loadExperimentAndResults(EXPERIMENT_MOCK.id);

        store.showExperimentSummary$.subscribe((value) => {
            expect(value).toEqual(true);
            done();
        });
    });
    describe('Bayesian response map hasSession = 0', () => {
        it('should summaryWinnerLegend$ get `NO_WINNER_FOUND` when experiment status `ENDED` and any winnerSuggestion', (done) => {
            dotExperimentsService.getById.mockReturnValue(
                of({
                    ...EXPERIMENT_MOCK,
                    status: DotExperimentStatusList.ENDED
                })
            );
            dotExperimentsService.getResults.mockReturnValue(
                of({
                    ...EXPERIMENT_MOCK_RESULTS,
                    sessions: { ...EXPERIMENT_MOCK_RESULTS.sessions, total: 0 }
                })
            );

            spectator.service.loadExperimentAndResults(EXPERIMENT_MOCK.id);

            store.state$.subscribe(({ experiment }) => {
                expect(experiment.status).toEqual(DotExperimentStatusList.ENDED);
            });
            store.summaryWinnerLegend$.subscribe((summaryWinnerLegend) => {
                expect(summaryWinnerLegend).toEqual(
                    ReportSummaryLegendByBayesianStatus.NO_WINNER_FOUND
                );
                done();
            });
        });

        it('should summaryWinnerLegend$ get `NO_ENOUGH_SESSIONS` when experiment status `RUNNING` and any winnerSuggestion', (done) => {
            dotExperimentsService.getById.mockReturnValue(
                of({
                    ...EXPERIMENT_MOCK,
                    status: DotExperimentStatusList.RUNNING
                })
            );
            dotExperimentsService.getResults.mockReturnValue(
                of({
                    ...EXPERIMENT_MOCK_RESULTS,
                    sessions: { ...EXPERIMENT_MOCK_RESULTS.sessions, total: 0 }
                })
            );

            spectator.service.loadExperimentAndResults(EXPERIMENT_MOCK.id);

            store.state$.subscribe(({ experiment }) => {
                expect(experiment.status).toEqual(DotExperimentStatusList.RUNNING);
            });
            store.summaryWinnerLegend$.subscribe((summaryWinnerLegend) => {
                expect(summaryWinnerLegend).toEqual(
                    ReportSummaryLegendByBayesianStatus.NO_ENOUGH_SESSIONS
                );
                done();
            });
        });
    });
    describe('Bayesian response map hasSession > 0', () => {
        it('should summaryWinnerLegend$ get `NO_WINNER_FOUND` when experiment status `ENDED` and winnerSuggestion=`TIE`', (done) => {
            dotExperimentsService.getById.mockReturnValue(
                of({
                    ...EXPERIMENT_MOCK,
                    status: DotExperimentStatusList.ENDED
                })
            );
            dotExperimentsService.getResults.mockReturnValue(
                of({
                    ...EXPERIMENT_MOCK_RESULTS,
                    sessions: { ...EXPERIMENT_MOCK_RESULTS.sessions, total: 10 },
                    bayesianResult: {
                        ...EXPERIMENT_MOCK_RESULTS.bayesianResult,
                        suggestedWinner: BayesianStatusResponse.TIE
                    }
                })
            );

            spectator.service.loadExperimentAndResults(EXPERIMENT_MOCK.id);

            store.state$.subscribe(({ experiment, results }) => {
                expect(experiment.status).toEqual(DotExperimentStatusList.ENDED);
                expect(results.bayesianResult.suggestedWinner).toEqual(BayesianStatusResponse.TIE);
            });
            store.summaryWinnerLegend$.subscribe((summaryWinnerLegend) => {
                expect(summaryWinnerLegend).toEqual(
                    ReportSummaryLegendByBayesianStatus.NO_WINNER_FOUND
                );
                done();
            });
        });

        it('should summaryWinnerLegend$ get `NO_WINNER_FOUND` when experiment status `RUNNING` and winnerSuggestion=`TIE`', (done) => {
            dotExperimentsService.getById.mockReturnValue(
                of({
                    ...EXPERIMENT_MOCK,
                    status: DotExperimentStatusList.RUNNING
                })
            );
            dotExperimentsService.getResults.mockReturnValue(
                of({
                    ...EXPERIMENT_MOCK_RESULTS,
                    sessions: { ...EXPERIMENT_MOCK_RESULTS.sessions, total: 10 },
                    bayesianResult: {
                        ...EXPERIMENT_MOCK_RESULTS.bayesianResult,
                        suggestedWinner: BayesianStatusResponse.TIE
                    }
                })
            );

            spectator.service.loadExperimentAndResults(EXPERIMENT_MOCK.id);

            store.state$.subscribe(({ experiment, results }) => {
                expect(experiment.status).toEqual(DotExperimentStatusList.RUNNING);
                expect(results.bayesianResult.suggestedWinner).toEqual(BayesianStatusResponse.TIE);
            });
            store.summaryWinnerLegend$.subscribe((summaryWinnerLegend) => {
                expect(summaryWinnerLegend).toEqual(
                    ReportSummaryLegendByBayesianStatus.NO_WINNER_FOUND
                );
                done();
            });
        });

        it('should summaryWinnerLegend$ get `NO_WINNER_FOUND` when experiment status `ENDED` and winnerSuggestion=`NONE`', (done) => {
            dotExperimentsService.getById.mockReturnValue(
                of({
                    ...EXPERIMENT_MOCK,
                    status: DotExperimentStatusList.ENDED
                })
            );
            dotExperimentsService.getResults.mockReturnValue(
                of({
                    ...EXPERIMENT_MOCK_RESULTS,
                    sessions: { ...EXPERIMENT_MOCK_RESULTS.sessions, total: 10 },
                    bayesianResult: {
                        ...EXPERIMENT_MOCK_RESULTS.bayesianResult,
                        suggestedWinner: BayesianStatusResponse.NONE
                    }
                })
            );

            spectator.service.loadExperimentAndResults(EXPERIMENT_MOCK.id);

            store.state$.subscribe(({ experiment, results }) => {
                expect(experiment.status).toEqual(DotExperimentStatusList.ENDED);
                expect(results.bayesianResult.suggestedWinner).toEqual(BayesianStatusResponse.NONE);
            });
            store.summaryWinnerLegend$.subscribe((summaryWinnerLegend) => {
                expect(summaryWinnerLegend).toEqual(
                    ReportSummaryLegendByBayesianStatus.NO_WINNER_FOUND
                );
                done();
            });
        });

        it('should summaryWinnerLegend$ get `NO_ENOUGH_SESSIONS` when experiment status `RUNNING` and winnerSuggestion=`NONE`', (done) => {
            dotExperimentsService.getById.mockReturnValue(
                of({
                    ...EXPERIMENT_MOCK,
                    status: DotExperimentStatusList.RUNNING
                })
            );
            dotExperimentsService.getResults.mockReturnValue(
                of({
                    ...EXPERIMENT_MOCK_RESULTS,
                    sessions: { ...EXPERIMENT_MOCK_RESULTS.sessions, total: 10 },
                    bayesianResult: {
                        ...EXPERIMENT_MOCK_RESULTS.bayesianResult,
                        suggestedWinner: BayesianStatusResponse.NONE
                    }
                })
            );

            spectator.service.loadExperimentAndResults(EXPERIMENT_MOCK.id);

            store.state$.subscribe(({ experiment, results }) => {
                expect(experiment.status).toEqual(DotExperimentStatusList.RUNNING);
                expect(results.bayesianResult.suggestedWinner).toEqual(BayesianStatusResponse.NONE);
            });
            store.summaryWinnerLegend$.subscribe((summaryWinnerLegend) => {
                expect(summaryWinnerLegend).toEqual(
                    ReportSummaryLegendByBayesianStatus.NO_ENOUGH_SESSIONS
                );
                done();
            });
        });

        it('should summaryWinnerLegend$ get `WINNER` when experiment status `ENDED` and winnerSuggestion has a variantId`', (done) => {
            const winnerVariantId = EXPERIMENT_MOCK.trafficProportion.variants[0].id;

            dotExperimentsService.getById.mockReturnValue(
                of({
                    ...EXPERIMENT_MOCK,
                    status: DotExperimentStatusList.ENDED
                })
            );
            dotExperimentsService.getResults.mockReturnValue(
                of({
                    ...EXPERIMENT_MOCK_RESULTS,
                    sessions: { ...EXPERIMENT_MOCK_RESULTS.sessions, total: 10 },
                    bayesianResult: {
                        ...EXPERIMENT_MOCK_RESULTS.bayesianResult,
                        suggestedWinner: winnerVariantId
                    }
                })
            );

            spectator.service.loadExperimentAndResults(EXPERIMENT_MOCK.id);

            store.state$.subscribe(({ experiment, results }) => {
                expect(experiment.status).toEqual(DotExperimentStatusList.ENDED);
                expect(results.bayesianResult.suggestedWinner).toEqual(winnerVariantId);
            });
            store.summaryWinnerLegend$.subscribe((summaryWinnerLegend) => {
                expect(summaryWinnerLegend).toEqual(ReportSummaryLegendByBayesianStatus.WINNER);
                done();
            });
        });

        it('should summaryWinnerLegend$ get `PRELIMINARY_WINNER` when experiment status `RUNNING` and winnerSuggestion has a variantId', (done) => {
            const winnerVariantId = EXPERIMENT_MOCK.trafficProportion.variants[0].id;

            dotExperimentsService.getById.mockReturnValue(
                of({
                    ...EXPERIMENT_MOCK,
                    status: DotExperimentStatusList.RUNNING
                })
            );
            dotExperimentsService.getResults.mockReturnValue(
                of({
                    ...EXPERIMENT_MOCK_RESULTS,
                    sessions: { ...EXPERIMENT_MOCK_RESULTS.sessions, total: 10 },
                    bayesianResult: {
                        ...EXPERIMENT_MOCK_RESULTS.bayesianResult,
                        suggestedWinner: winnerVariantId
                    }
                })
            );

            spectator.service.loadExperimentAndResults(EXPERIMENT_MOCK.id);

            store.state$.subscribe(({ experiment, results }) => {
                expect(experiment.status).toEqual(DotExperimentStatusList.RUNNING);
                expect(results.bayesianResult.suggestedWinner).toEqual(winnerVariantId);
            });
            store.summaryWinnerLegend$.subscribe((summaryWinnerLegend) => {
                expect(summaryWinnerLegend).toEqual(
                    ReportSummaryLegendByBayesianStatus.PRELIMINARY_WINNER
                );
                done();
            });
        });
    });

    describe('Effects', () => {
        it('should load experiment to store', (done) => {
            dotExperimentsService.getById.mockReturnValue(of(EXPERIMENT_MOCK));

            store.loadExperimentAndResults(EXPERIMENT_MOCK.id);

            expect(dotExperimentsService.getById).toHaveBeenCalledWith(EXPERIMENT_MOCK.id);

            store.state$.subscribe(({ experiment }) => {
                expect(experiment).toEqual(EXPERIMENT_MOCK);
                done();
            });
        });

        it('should promote variant', () => {
            const variant: DotExperimentVariantDetail = {
                id: '111',
                name: 'Variant 111 Name',
                conversions: 0,
                conversionRate: '0%',
                conversionRateRange: '19.41% to 93.24%',
                sessions: 0,
                probabilityToBeBest: '7.69%',
                isWinner: false,
                isPromoted: false
            };

            dotExperimentsService.promoteVariant.mockReturnValue(of(EXPERIMENT_MOCK));

            store.promoteVariant({ experimentId: EXPERIMENT_MOCK.id, variant: variant });

            expect(dotExperimentsService.promoteVariant).toHaveBeenCalledWith(
                EXPERIMENT_MOCK.id,
                variant.id
            );
        });
    });

    describe('chartJs parser', () => {
        beforeEach(() => {
            dotExperimentsService.getById.mockReturnValue(
                of({
                    ...EXPERIMENT_MOCK,
                    status: DotExperimentStatusList.RUNNING
                })
            );
            spectator.service.loadExperimentAndResults(EXPERIMENT_MOCK.id);
        });

        it('should get all the xLabels', (done) => {
            const expectedXLabels = [
                ['Saturday', '04/01/2023'],
                ['Sunday', '04/02/2023'],
                ['Monday', '04/03/2023'],
                ['Tuesday', '04/04/2023'],
                ['Wednesday', '04/05/2023'],
                ['Thursday', '04/06/2023'],
                ['Friday', '04/07/2023'],
                ['Saturday', '04/08/2023'],
                ['Sunday', '04/09/2023'],
                ['Monday', '04/10/2023'],
                ['Tuesday', '04/11/2023'],
                ['Wednesday', '04/12/2023'],
                ['Thursday', '04/13/2023'],
                ['Friday', '04/14/2023'],
                ['Saturday', '04/15/2023']
            ];

            store.getChartData$.subscribe(({ labels }) => {
                expect(labels.length).toEqual(expectedXLabels.length);
                expect(labels).toEqual(expectedXLabels);
                done();
            });
        });

        it('should has 2 datasets', (done) => {
            store.getChartData$.subscribe(({ datasets }) => {
                expect(datasets.length).toEqual(
                    Object.keys(EXPERIMENT_MOCK_RESULTS.goals.primary.variants).length
                );
                done();
            });
        });

        it('should has a label and data properly parsed for each dataset', (done) => {
            const expectedDataByDataset = [
                [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15],
                [15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3, 2, 1]
            ];
            const expectedLabel = [
                EXPERIMENT_MOCK_RESULTS.goals.primary.variants.DEFAULT.variantDescription,
                EXPERIMENT_MOCK_RESULTS.goals.primary.variants['111'].variantDescription
            ];

            store.getChartData$.subscribe(({ datasets }) => {
                datasets.forEach((dataset, index) => {
                    const { label, data } = dataset;

                    expect(data).toEqual(expectedDataByDataset[index]);
                    expect(label).toEqual(expectedLabel[index]);
                });

                done();
            });
        });
    });
});
