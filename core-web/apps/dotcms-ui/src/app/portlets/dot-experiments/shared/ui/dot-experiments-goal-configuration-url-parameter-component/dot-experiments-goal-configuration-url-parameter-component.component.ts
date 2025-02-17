import { NgForOf } from '@angular/common';
import { ChangeDetectionStrategy, Component } from '@angular/core';
import { FormGroupDirective, ReactiveFormsModule } from '@angular/forms';

import { DropdownModule } from 'primeng/dropdown';
import { InputTextModule } from 'primeng/inputtext';

import { DotFieldValidationMessageModule } from '@components/_common/dot-field-validation-message/dot-file-validation-message.module';
import { DotAutofocusModule } from '@directives/dot-autofocus/dot-autofocus.module';
import { GoalsConditionsOperatorsListByType } from '@dotcms/dotcms-models';
import { DotFieldRequiredDirective, DotMessagePipeModule } from '@dotcms/ui';
import { DotExperimentsOptionContentBaseComponent } from '@portlets/dot-experiments/shared/ui/dot-experiment-options/components/dot-experiments-option-content-base-component/dot-experiments-option-content-base.component';
import { DotDropdownDirective } from '@portlets/shared/directives/dot-dropdown.directive';

@Component({
    standalone: true,
    selector: 'dot-experiments-goal-configuration-url-parameter-component',
    templateUrl: './dot-experiments-goal-configuration-url-parameter-component.component.html',
    styleUrls: ['./dot-experiments-goal-configuration-url-parameter-component.component.scss'],
    imports: [
        ReactiveFormsModule,
        NgForOf,
        DotDropdownDirective,
        DotFieldValidationMessageModule,
        DotMessagePipeModule,
        DropdownModule,
        DotFieldRequiredDirective,
        DotAutofocusModule,
        InputTextModule
    ],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class DotExperimentsGoalConfigurationUrlParameterComponentComponent extends DotExperimentsOptionContentBaseComponent {
    override operatorsList = GoalsConditionsOperatorsListByType['URL_PARAMETER'];

    constructor(private readonly fgd: FormGroupDirective) {
        super(fgd);
    }
}
