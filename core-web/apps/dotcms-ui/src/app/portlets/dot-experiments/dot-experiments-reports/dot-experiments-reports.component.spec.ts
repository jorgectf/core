import { createComponentFactory, mockProvider, Spectator } from '@ngneat/spectator';
import { of } from 'rxjs';

import { ActivatedRoute } from '@angular/router';

import { MessageService } from 'primeng/api';

import { DotMessageService } from '@dotcms/data-access';
import { ComponentStatus } from '@dotcms/dotcms-models';
import { MockDotMessageService } from '@dotcms/utils-testing';
import { DotExperimentsReportsSkeletonComponent } from '@portlets/dot-experiments/dot-experiments-reports/components/dot-experiments-reports-skeleton/dot-experiments-reports-skeleton.component';
import {
    DotExperimentsReportsStore,
    VmReportExperiment
} from '@portlets/dot-experiments/dot-experiments-reports/store/dot-experiments-reports-store';
import { DotExperimentsService } from '@portlets/dot-experiments/shared/services/dot-experiments.service';
import { DotExperimentsUiHeaderComponent } from '@portlets/dot-experiments/shared/ui/dot-experiments-header/dot-experiments-ui-header.component';
import {
    DotExperimentsReportsStoreMock,
    getExperimentMock
} from '@portlets/dot-experiments/test/mocks';
import { DotHttpErrorManagerService } from '@services/dot-http-error-manager/dot-http-error-manager.service';

import { DotExperimentsReportsComponent } from './dot-experiments-reports.component';

const ActivatedRouteMock = {
    snapshot: {
        params: {
            experimentId: '1111'
        }
    }
};

const defaultVmMock: VmReportExperiment = {
    experiment: getExperimentMock(0),

    isLoading: false,
    showSummary: false,
    status: ComponentStatus.INIT
};

const messageServiceMock = new MockDotMessageService({
    'experiments.configure.scheduling.name': 'xx'
});

describe('DotExperimentsReportsComponent', () => {
    let spectator: Spectator<DotExperimentsReportsComponent>;
    const createComponent = createComponentFactory({
        imports: [DotExperimentsUiHeaderComponent, DotExperimentsReportsSkeletonComponent],
        component: DotExperimentsReportsComponent,
        componentProviders: [
            mockProvider(DotExperimentsReportsStore, DotExperimentsReportsStoreMock)
        ],
        declarations: [],
        providers: [
            {
                provide: ActivatedRoute,
                useValue: ActivatedRouteMock
            },
            {
                provide: DotMessageService,
                useValue: messageServiceMock
            },
            mockProvider(DotExperimentsService),
            mockProvider(DotHttpErrorManagerService),
            mockProvider(MessageService)
        ]
    });

    beforeEach(() => {
        spectator = createComponent({
            detectChanges: false
        });
    });

    it('should show the skeleton component when is loading', () => {
        spectator.component.vm$ = of({ ...defaultVmMock, isLoading: true });
        spectator.detectChanges();

        expect(spectator.query(DotExperimentsUiHeaderComponent)).toExist();
        expect(spectator.query(DotExperimentsReportsSkeletonComponent)).toExist();
    });

    it("shouldn't show the skeleton component when is not loading", () => {
        spectator.component.vm$ = of({ ...defaultVmMock, isLoading: false });
        spectator.detectChanges();

        expect(spectator.query(DotExperimentsUiHeaderComponent)).toExist();
        expect(spectator.query(DotExperimentsReportsSkeletonComponent)).not.toExist();
    });
});
