import { Component, Input, Output, EventEmitter, Injectable, DebugElement } from '@angular/core';
import { DOTTestBed } from 'src/app/test/dot-test-bed';
import { CardinalitySelectorComponent } from './cardinality-selector.component';
import { DotMessageService } from '@services/dot-messages-service';
import { MockDotMessageService } from 'src/app/test/dot-message-service.mock';
import { DotRelationshipCardinality } from '@portlets/content-types/fields/shared/dot-relationship-cardinality.model';
import { Observable, of } from 'rxjs';
import { RelationshipService } from '@portlets/content-types/fields/service/relationship.service';
import { ComponentFixture } from '@angular/core/testing';
import { By } from '@angular/platform-browser';

const cardinalities = [
    {
        label: 'Many to many',
        id: 0,
        name: 'MANY_TO_MANY'
    },
    {
        label: 'One to one',
        id: 1,
        name: 'ONE_TO_ONE'
    }
];

@Component({
    selector: 'dot-host-component',
    template: `<dot-cardinality-selector [cardinalityIndex] = "cardinalityIndex"
                                         [disabled] = "disabled">
               </dot-cardinality-selector>`
})
class HostTestComponent {
    @Input()
    cardinalityIndex: number;

    @Input()
    disabled: boolean;

    @Output()
    change: EventEmitter<DotRelationshipCardinality> = new EventEmitter();
}


@Injectable()
class MockRelationshipService {
    loadCardinalities(): Observable<DotRelationshipCardinality[]> {
        return of(cardinalities);
    }
}

describe('CardinalitySelectorComponent', () => {

    let fixtureHostComponent: ComponentFixture<HostTestComponent>;
    let comp: CardinalitySelectorComponent;
    let de: DebugElement;

    const messageServiceMock = new MockDotMessageService({
        'contenttypes.field.properties.relationship.cardinality.placeholder': 'Select Cardinality',
    });

    beforeEach(() => {
        DOTTestBed.configureTestingModule({
            declarations: [
                CardinalitySelectorComponent,
                HostTestComponent
            ],
            imports: [],
            providers: [
                { provide: DotMessageService, useValue: messageServiceMock },
                { provide: RelationshipService, useClass: MockRelationshipService }
            ]
        });

        fixtureHostComponent = DOTTestBed.createComponent(HostTestComponent);
        de = fixtureHostComponent.debugElement.query(By.css('dot-cardinality-selector'));
        comp = de.componentInstance;
    });

    it('should have a p-dropdown with right attributes', () => {
        fixtureHostComponent.detectChanges();

        const dropdown = de.query(By.css('p-dropdown'));

        expect(dropdown).not.toBeUndefined();

        expect(dropdown.componentInstance.appendTo).toBe('body');
        expect(dropdown.componentInstance.optionLabel).toBe('label');
    });

    it('should disabled p-dropdown', () => {
        fixtureHostComponent.componentInstance.disabled = true;
        fixtureHostComponent.detectChanges();

        const dropdown = de.query(By.css('p-dropdown'));

        expect(dropdown.componentInstance.disabled).toBeTruthy();
    });

    it('should load cardinalities', () => {
        fixtureHostComponent.componentInstance.disabled = true;
        fixtureHostComponent.detectChanges();

        const  dropdown = de.query(By.css('p-dropdown'));

        expect(comp.cardinalities).toEqual(cardinalities);
        expect(dropdown.componentInstance.options).toEqual(dropdown.componentInstance.options);
    });

    it('should trigger a change event p-dropdown', (done) => {
        fixtureHostComponent.detectChanges();

        comp.change.subscribe((change) => {
            expect(change).toEqual(cardinalities[1]);
            done();
        });

        const dropdown = de.query(By.css('p-dropdown'));
        dropdown.triggerEventHandler('onChange', cardinalities[1]);
    });
});
