import { LowerCasePipe, NgIf } from '@angular/common';
import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { RouterLink } from '@angular/router';

import { SkeletonModule } from 'primeng/skeleton';
import { TagModule } from 'primeng/tag';

import { DotExperimentStatusList } from '@dotcms/dotcms-models';
import { DotIconModule } from '@dotcms/ui';
import { DotPipesModule } from '@pipes/dot-pipes.module';

@Component({
    standalone: true,
    selector: 'dot-experiments-header',
    templateUrl: './dot-experiments-ui-header.component.html',
    styleUrls: ['./dot-experiments-ui-header.component.scss'],
    imports: [
        RouterLink,
        NgIf,
        LowerCasePipe,
        // DotCMS
        DotIconModule,
        DotPipesModule,
        // PrimeNG
        SkeletonModule,
        TagModule
    ],
    changeDetection: ChangeDetectionStrategy.OnPush
})
export class DotExperimentsUiHeaderComponent {
    @Input()
    title = '';

    @Input()
    isLoading: boolean;

    @Input()
    status: DotExperimentStatusList;

    @Output()
    goBack = new EventEmitter<boolean>();
}
