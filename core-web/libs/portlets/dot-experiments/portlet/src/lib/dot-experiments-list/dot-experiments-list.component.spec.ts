import {
    byTestId,
    createComponentFactory,
    mockProvider,
    Spectator,
    SpyObject
} from '@ngneat/spectator/jest';
import { provideComponentStore } from '@ngrx/component-store';
import { of } from 'rxjs';

import { ActivatedRoute, Router } from '@angular/router';

import { ConfirmationService, MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { ConfirmPopupModule } from 'primeng/confirmpopup';
import { SkeletonModule } from 'primeng/skeleton';

import { UiDotIconButtonTooltipModule } from '@components/_common/dot-icon-button-tooltip/dot-icon-button-tooltip.module';
import { DotMessageService } from '@dotcms/data-access';
import { ComponentStatus, DotExperimentStatusList } from '@dotcms/dotcms-models';
import { DotExperimentsService } from '@dotcms/portlets/dot-experiments/data-access';
import {
    DotIconModule,
    DotMessagePipeModule,
    DotMessagePipe,
    UiDotIconButtonModule
} from '@dotcms/ui';
import {
    ActivatedRouteListStoreMock,
    DotExperimentsStoreMock,
    getExperimentAllMocks,
    getExperimentMock
} from '@dotcms/utils-testing';
import { DotDynamicDirective } from '@portlets/shared/directives/dot-dynamic.directive';
import { DotFormatDateService } from '@services/dot-format-date-service';
import { DotHttpErrorManagerService } from '@services/dot-http-error-manager/dot-http-error-manager.service';

import { DotExperimentsCreateComponent } from './components/dot-experiments-create/dot-experiments-create.component';
import { DotExperimentsEmptyExperimentsComponent } from './components/dot-experiments-empty-experiments/dot-experiments-empty-experiments.component';
import { DotExperimentsListSkeletonComponent } from './components/dot-experiments-list-skeleton/dot-experiments-list-skeleton.component';
import { DotExperimentsStatusFilterComponent } from './components/dot-experiments-status-filter/dot-experiments-status-filter.component';
import { DotExperimentsListComponent } from './dot-experiments-list.component';
import { DotExperimentsListStore, VmListExperiments } from './store/dot-experiments-list-store';

import { DotExperimentsStore } from '../dot-experiments-shell/store/dot-experiments.store';

const EXPERIMENT_MOCKS = getExperimentAllMocks();

const EXPERIMENT_MOCK = getExperimentMock(0);

describe('ExperimentsListComponent', () => {
    let spectator: Spectator<DotExperimentsListComponent>;
    let dotExperimentsStatusFilterComponent: DotExperimentsStatusFilterComponent;
    let dotExperimentsEmptyExperimentsComponent: DotExperimentsEmptyExperimentsComponent;
    let dotExperimentsListSkeletonComponent: DotExperimentsListSkeletonComponent;

    let router: SpyObject<Router>;

    const createComponent = createComponentFactory({
        imports: [
            DotMessagePipeModule,
            SkeletonModule,
            DotIconModule,
            UiDotIconButtonTooltipModule,
            UiDotIconButtonModule,
            DotExperimentsCreateComponent,
            DotDynamicDirective,
            ButtonModule,
            DotExperimentsStatusFilterComponent,
            DotExperimentsEmptyExperimentsComponent,
            DotExperimentsListSkeletonComponent,
            ConfirmPopupModule
        ],
        component: DotExperimentsListComponent,
        componentProviders: [provideComponentStore(DotExperimentsListStore)],

        providers: [
            ConfirmationService,
            mockProvider(DotExperimentsStore, DotExperimentsStoreMock),
            mockProvider(Router),
            mockProvider(DotMessagePipe),
            mockProvider(DotMessageService),
            mockProvider(DotFormatDateService),
            mockProvider(DotExperimentsService, {
                getAll: () => of(EXPERIMENT_MOCKS)
            }),
            mockProvider(MessageService),
            mockProvider(DotHttpErrorManagerService),
            {
                provide: ActivatedRoute,
                useClass: ActivatedRouteListStoreMock
            }
        ]
    });

    beforeEach(() => {
        spectator = createComponent({
            detectChanges: false
        });

        router = spectator.inject(Router);
    });

    it('should show the skeleton component when is loading', () => {
        const vmListExperimentsMock$: VmListExperiments = {
            pageId: '',
            pageTitle: '',
            experiments: [],
            filterStatus: [],
            experimentsFiltered: [],
            isLoading: true,
            sidebar: {
                status: ComponentStatus.IDLE,
                isOpen: false
            }
        };
        spectator.component.vm$ = of(vmListExperimentsMock$);
        spectator.detectComponentChanges();

        dotExperimentsListSkeletonComponent = spectator.query(DotExperimentsListSkeletonComponent);
        expect(dotExperimentsListSkeletonComponent).toExist();
    });

    it('should show the empty component when is not loading and no experiments', () => {
        const vmListExperimentsMock$: VmListExperiments = {
            pageId: '',
            pageTitle: '',
            experiments: [],
            filterStatus: [],
            experimentsFiltered: [],
            isLoading: false,
            sidebar: {
                status: ComponentStatus.IDLE,
                isOpen: false
            }
        };
        spectator.component.vm$ = of(vmListExperimentsMock$);
        spectator.detectComponentChanges();

        dotExperimentsEmptyExperimentsComponent = spectator.query(
            DotExperimentsEmptyExperimentsComponent
        );
        expect(dotExperimentsEmptyExperimentsComponent).toExist();
    });

    it('should show the filters component and add experiment button exist when has experiments', () => {
        const vmListExperimentsMock$: VmListExperiments = {
            pageId: '',
            pageTitle: '',
            experiments: getExperimentAllMocks(),
            filterStatus: [],
            experimentsFiltered: [],
            isLoading: false,
            sidebar: {
                status: ComponentStatus.IDLE,
                isOpen: false
            }
        };
        spectator.component.vm$ = of(vmListExperimentsMock$);
        spectator.detectComponentChanges();

        dotExperimentsStatusFilterComponent = spectator.query(DotExperimentsStatusFilterComponent);
        expect(dotExperimentsStatusFilterComponent).toExist();

        const addExperimentButton = spectator.query(byTestId('add-experiment-button'));
        expect(addExperimentButton).toExist();
    });

    it('should show the sidebar when click ADD EXPERIMENT', () => {
        spectator.detectChanges();
        spectator.component.addExperiment();
        spectator.detectComponentChanges();

        expect(spectator.query(DotExperimentsCreateComponent)).toExist();
    });

    it('should go to Configuration Container', () => {
        spectator.detectComponentChanges();
        spectator.component.gotToConfigurationAction(EXPERIMENT_MOCK);
        expect(router.navigate).toHaveBeenCalledWith(
            [
                '/edit-page/experiments/',
                EXPERIMENT_MOCK.pageId,
                EXPERIMENT_MOCK.id,
                'configuration'
            ],
            {
                queryParams: {
                    mode: null,
                    variantName: null,
                    experimentId: null
                },
                queryParamsHandling: 'merge'
            }
        );
    });

    it('should go to report Container if the experiment status is RUNNING', () => {
        spectator.detectComponentChanges();
        spectator.component.goToContainerAction({
            ...EXPERIMENT_MOCK,
            status: DotExperimentStatusList.RUNNING
        });
        expect(router.navigate).toHaveBeenCalledWith(
            ['/edit-page/experiments/', EXPERIMENT_MOCK.pageId, EXPERIMENT_MOCK.id, 'reports'],
            {
                queryParams: {
                    mode: null,
                    variantName: null,
                    experimentId: null
                },
                queryParamsHandling: 'merge'
            }
        );
    });

    it('should go to report Container if the experiment status is ENDED', () => {
        spectator.detectComponentChanges();
        spectator.component.goToContainerAction({
            ...EXPERIMENT_MOCK,
            status: DotExperimentStatusList.ENDED
        });
        expect(router.navigate).toHaveBeenCalledWith(
            ['/edit-page/experiments/', EXPERIMENT_MOCK.pageId, EXPERIMENT_MOCK.id, 'reports'],
            {
                queryParams: {
                    mode: null,
                    variantName: null,
                    experimentId: null
                },
                queryParamsHandling: 'merge'
            }
        );
    });

    it('should go to configuration Container if the experiment status is DRAFT', () => {
        spectator.detectComponentChanges();
        spectator.component.goToContainerAction({
            ...EXPERIMENT_MOCK,
            status: DotExperimentStatusList.DRAFT
        });
        expect(router.navigate).toHaveBeenCalledWith(
            [
                '/edit-page/experiments/',
                EXPERIMENT_MOCK.pageId,
                EXPERIMENT_MOCK.id,
                'configuration'
            ],
            {
                queryParams: {
                    mode: null,
                    variantName: null,
                    experimentId: null
                },
                queryParamsHandling: 'merge'
            }
        );
    });

    it('should go to configuration Container if the experiment status is SCHEDULED', () => {
        spectator.detectComponentChanges();
        spectator.component.goToContainerAction({
            ...EXPERIMENT_MOCK,
            status: DotExperimentStatusList.SCHEDULED
        });
        expect(router.navigate).toHaveBeenCalledWith(
            [
                '/edit-page/experiments/',
                EXPERIMENT_MOCK.pageId,
                EXPERIMENT_MOCK.id,
                'configuration'
            ],
            {
                queryParams: {
                    mode: null,
                    variantName: null,
                    experimentId: null
                },
                queryParamsHandling: 'merge'
            }
        );
    });
});
