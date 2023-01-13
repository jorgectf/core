import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';

import { Observable } from 'rxjs';


import { DotRelationshipCardinality } from '@portlets/shared/dot-content-types-edit/components/fields/content-type-fields-properties-form/field-properties/dot-relationships-property/model/dot-relationship-cardinality.model';
import { DotRelationshipService } from '@portlets/shared/dot-content-types-edit/components/fields/content-type-fields-properties-form/field-properties/dot-relationships-property/services/dot-relationship.service';

/**
 *Selector for relationships cardinalities
 *
 * @export
 * @class DotCardinalitySelectorComponent
 * @implements {OnInit}
 * @implements {OnChanges}
 */
@Component({
    providers: [],
    selector: 'dot-cardinality-selector',
    templateUrl: './dot-cardinality-selector.component.html',
    styleUrls: ['./dot-cardinality-selector.component.scss']
})
export class DotCardinalitySelectorComponent implements OnInit {
    @Input()
    value: number;

    @Input()
    disabled: boolean;

    @Output()
    switch: EventEmitter<number> = new EventEmitter();

    options: Observable<DotRelationshipCardinality[]>;

    constructor(private dotRelationshipService: DotRelationshipService) {}

    ngOnInit() {
        this.options = this.dotRelationshipService.loadCardinalities();
    }
}
