import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { MultiSelectModule } from 'primeng/multiselect';

import { DotDropdownSelectOption, DotExperimentStatusList } from '@dotcms/dotcms-models';
import { DotMessagePipeModule } from '@dotcms/ui';
import { DotDropdownDirective } from '@portlets/shared/directives/dot-dropdown.directive';

@Component({
    standalone: true,
    selector: 'dot-experiments-status-filter',
    imports: [
        FormsModule,
        // dotCMS
        DotMessagePipeModule,
        DotDropdownDirective,
        // PrimeNG
        MultiSelectModule
    ],
    templateUrl: './dot-experiments-status-filter.component.html',
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class DotExperimentsStatusFilterComponent {
    @Input()
    selectedItems: Array<string>;
    @Input()
    options: Array<DotDropdownSelectOption<string>>;

    @Output()
    switch = new EventEmitter<DotExperimentStatusList[]>();
}
