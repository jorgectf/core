import { Observable, Subject } from 'rxjs';

import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, ComponentRef, ViewChild } from '@angular/core';

import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';

import { tap } from 'rxjs/operators';

import {
    ExperimentSteps,
    Goals,
    GOALS_METADATA_MAP,
    Status,
    StepStatus
} from '@dotcms/dotcms-models';
import { DotIconModule } from '@dotcms/ui';
import { DotMessagePipeModule } from '@pipes/dot-message/dot-message-pipe.module';
import { DotExperimentsConfigurationGoalSelectComponent } from '@portlets/dot-experiments/dot-experiments-configuration/components/dot-experiments-configuration-goal-select/dot-experiments-configuration-goal-select.component';
import { DotExperimentsConfigurationStore } from '@portlets/dot-experiments/dot-experiments-configuration/store/dot-experiments-configuration-store';
import { DotDynamicDirective } from '@portlets/shared/directives/dot-dynamic.directive';

/**
 * Assign goal to experiment
 */
@Component({
    selector: 'dot-experiments-configuration-goals',
    standalone: true,
    imports: [
        CommonModule,

        DotMessagePipeModule,
        DotDynamicDirective,
        DotIconModule,
        // PrimeNg
        ButtonModule,
        CardModule
    ],
    templateUrl: './dot-experiments-configuration-goals.component.html',
    styleUrls: ['./dot-experiments-configuration-goals.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class DotExperimentsConfigurationGoalsComponent {
    vm$: Observable<{ goals: Goals; status: StepStatus }> =
        this.dotExperimentsConfigurationStore.goalsStepVm$.pipe(
            tap(({ status }) => this.handleSidebar(status))
        );

    goalTypeMap = GOALS_METADATA_MAP;
    destroy$: Subject<boolean> = new Subject<boolean>();
    @ViewChild(DotDynamicDirective, { static: true }) sidebarHost!: DotDynamicDirective;
    private componentRef: ComponentRef<DotExperimentsConfigurationGoalSelectComponent>;

    constructor(
        private readonly dotExperimentsConfigurationStore: DotExperimentsConfigurationStore
    ) {}

    /**
     * Open the sidebar to select the principal goal
     * @returns void
     * @memberof DotExperimentsConfigurationGoalsComponent
     */
    openSelectGoalSidebar() {
        this.dotExperimentsConfigurationStore.openSidebar(ExperimentSteps.GOAL);
    }

    private handleSidebar(status: StepStatus) {
        if (status && status.isOpen) {
            this.loadSidebarComponent(status);
        } else {
            this.removeSidebarComponent();
        }
    }

    private loadSidebarComponent(status: StepStatus): void {
        if (status && status.isOpen && status.status != Status.SAVING) {
            this.sidebarHost.viewContainerRef.clear();
            this.componentRef =
                this.sidebarHost.viewContainerRef.createComponent<DotExperimentsConfigurationGoalSelectComponent>(
                    DotExperimentsConfigurationGoalSelectComponent
                );
        }
    }

    private removeSidebarComponent() {
        if (this.componentRef) {
            this.sidebarHost.viewContainerRef.clear();
        }
    }
}
