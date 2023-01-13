
import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanDeactivate, RouterStateSnapshot } from '@angular/router';

import { Observable, Observer, of as observableOf } from 'rxjs';

import { DotAlertConfirmService } from '@dotcms/data-access';

import { OnSaveDeactivate } from './save-on-deactivate';

@Injectable()
export class DotSaveOnDeactivateService implements CanDeactivate<OnSaveDeactivate> {
    constructor(private dotDialogService: DotAlertConfirmService) {}

    canDeactivate(
        component: OnSaveDeactivate,
        _route: ActivatedRouteSnapshot,
        _state: RouterStateSnapshot
    ): Observable<boolean> {
        if (component.shouldSaveBefore()) {
            return Observable.create((observer: Observer<boolean>) => {
                this.dotDialogService.confirm({
                    accept: () => {
                        component.onDeactivateSave().subscribe((res) => {
                            observer.next(res);
                            observer.complete();
                        });
                    },
                    reject: () => {
                        observer.next(true);
                        observer.complete();
                    },
                    header: component.getSaveWarningMessages().header,
                    message: component.getSaveWarningMessages().message
                });
            });
        } else {
            return observableOf(true);
        }
    }
}
