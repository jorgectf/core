import { Observable } from 'rxjs';

import { Injectable } from '@angular/core';

import { pluck } from 'rxjs/operators';

import { CoreWebService, DotRequestOptionsArgs } from '@dotcms/dotcms-js';
import { DotPageContainer, DotWhatChanged, DotCopyContentInPage } from '@dotcms/dotcms-models';

import { DotSessionStorageService } from '../dot-session-storage/dot-session-storage.service';

@Injectable()
export class DotEditPageService {
    constructor(
        private coreWebService: CoreWebService,
        private readonly dotSessionStorageService: DotSessionStorageService
    ) {}

    /**
     * Save a page's content
     *
     * @param string pageId
     * @param DotPageContainer[] content
     * @returns Observable<string>
     * @memberof DotEditPageService
     */
    save(pageId: string, content: DotPageContainer[]): Observable<string> {
        const requestOptions: DotRequestOptionsArgs = {
            method: 'POST',
            body: content,
            url: `v1/page/${pageId}/content`
        };

        const currentVariantName = this.dotSessionStorageService.getVariationId();

        if (currentVariantName) {
            requestOptions.params = {
                variantName: currentVariantName
            };
        }

        return this.coreWebService.requestView(requestOptions).pipe(pluck('entity'));
    }

    /**
     * Get the live and working version markup of specific page.
     *
     * @param string pageId
     * @param string language
     * @returns Observable<DotWhatChanged>
     * @memberof DotEditPageService
     */
    whatChange(pageId: string, languageId: string): Observable<DotWhatChanged> {
        return this.coreWebService
            .requestView({
                url: `v1/page/${pageId}/render/versions?langId=${languageId}`
            })
            .pipe(pluck('entity'));
    }

    /**
     *
     *
     * @param string pageId
     * @return {*}
     * @memberof DotEditPageService
     */
    copyContentInPage(data: DotCopyContentInPage): Observable<unknown> {
        const requestOptions: DotRequestOptionsArgs = {
            method: 'POST',
            body: data,
            url: `api/v1/page/copyContent`
        };

        return this.coreWebService.requestView(requestOptions).pipe(pluck('entity'));
    }
}
