import { Observable } from 'rxjs';

import { CommonModule } from '@angular/common';
import { ChangeDetectionStrategy, Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';

import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { SidebarModule } from 'primeng/sidebar';

import { DotFieldValidationMessageModule } from '@components/_common/dot-field-validation-message/dot-file-validation-message.module';
import { DotAutofocusModule } from '@directives/dot-autofocus/dot-autofocus.module';
import {
    ComponentStatus,
    MAX_INPUT_LENGTH,
    StepStatus,
    TrafficProportion
} from '@dotcms/dotcms-models';
import { DotMessagePipeModule } from '@pipes/dot-message/dot-message-pipe.module';
import { DotExperimentsConfigurationStore } from '@portlets/dot-experiments/dot-experiments-configuration/store/dot-experiments-configuration-store';
import { DotSidebarDirective } from '@portlets/shared/directives/dot-sidebar.directive';
import { DotSidebarHeaderComponent } from '@shared/dot-sidebar-header/dot-sidebar-header.component';

@Component({
    selector: 'dot-experiments-configuration-variants-add',
    standalone: true,
    imports: [
        CommonModule,
        ReactiveFormsModule,

        DotSidebarHeaderComponent,
        DotMessagePipeModule,
        DotFieldValidationMessageModule,
        DotSidebarDirective,
        DotAutofocusModule,

        //PrimeNg
        SidebarModule,
        ButtonModule,
        InputTextModule
    ],
    templateUrl: './dot-experiments-configuration-variants-add.component.html',
    styleUrls: ['./dot-experiments-configuration-variants-add.component.scss'],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class DotExperimentsConfigurationVariantsAddComponent implements OnInit {
    protected readonly maxNameLength = MAX_INPUT_LENGTH;
    stepStatus = ComponentStatus;

    form: FormGroup;

    vm$: Observable<{
        experimentId: string;
        trafficProportion: TrafficProportion;
        status: StepStatus;
        isExperimentADraft: boolean;
    }> = this.dotExperimentsConfigurationStore.variantsStepVm$;

    constructor(
        private readonly dotExperimentsConfigurationStore: DotExperimentsConfigurationStore
    ) {}

    ngOnInit(): void {
        this.initForm();
    }

    /**
     * Save variant
     * @param {string} experimentId
     * @returns void
     * @memberof DotExperimentsConfigurationVariantsAddComponent
     */
    saveVariant(experimentId: string) {
        this.dotExperimentsConfigurationStore.addVariant({
            name: this.form.value.name,
            experimentId
        });
        this.form.reset();
    }

    /**
     * Close sidebar
     * @returns void
     * @memberof DotExperimentsConfigurationVariantsAddComponent
     */
    closeSidebar() {
        this.dotExperimentsConfigurationStore.closeSidebar();
    }

    private initForm() {
        this.form = new FormGroup({
            name: new FormControl<string>('', {
                nonNullable: true,
                validators: [Validators.required, Validators.maxLength(50)]
            })
        });
    }
}
