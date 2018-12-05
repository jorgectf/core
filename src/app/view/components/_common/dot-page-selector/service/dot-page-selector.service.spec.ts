import { DotPageSelectorService } from './dot-page-selector.service';
import { DOTTestBed } from '../../../../../test/dot-test-bed';
import { ConnectionBackend, ResponseOptions, Response } from '@angular/http';
import { MockBackend } from '@angular/http/testing';
import {
    mockDotPageSelectorResults,
    mockDotSiteSelectorResults
} from '../dot-page-selector.component.spec';

describe('Service: DotPageSelector', () => {
    beforeEach(() => {
        this.injector = DOTTestBed.resolveAndCreate([DotPageSelectorService]);
        this.dotPageSelectorService = this.injector.get(DotPageSelectorService);
        this.backend = this.injector.get(ConnectionBackend) as MockBackend;
        this.backend.connections.subscribe((connection: any) => (this.lastConnection = connection));
    });

    it('should make page search', () => {
        let result;
        const searchParam = 'about-us';
        const query = {
            query: {
                query_string: {
                    query: `+basetype:5 +path:*${searchParam}*`
                }
            }
        };
        this.dotPageSelectorService.search(searchParam).subscribe(res => {
            result = res;
        });

        this.lastConnection.mockRespond(
            new Response(
                new ResponseOptions({
                    body: {
                        contentlets: [mockDotPageSelectorResults.data[0].payload]
                    }
                })
            )
        );

        expect(result).toEqual(mockDotPageSelectorResults);
        expect(this.lastConnection.request.url).toContain('es/search');
        expect(this.lastConnection.request.method).toEqual(1);
        expect(this.lastConnection.request._body).toEqual(query);
    });

    it('should make a host search', () => {
        let result;
        const query = {
            query: {
                query_string: {
                    query: `+contenttype:Host +host.hostName:*demo.dotcms.com*`
                }
            }
        };
        this.dotPageSelectorService.search('//demo.dotcms.com').subscribe(res => {
            result = res;
        });

        this.lastConnection.mockRespond(
            new Response(
                new ResponseOptions({
                    body: {
                        contentlets: [mockDotSiteSelectorResults.data[0].payload]
                    }
                })
            )
        );
        expect(result).toEqual(mockDotSiteSelectorResults);
        expect(this.lastConnection.request.url).toContain('es/search');
        expect(this.lastConnection.request.method).toEqual(1);
        expect(this.lastConnection.request._body).toEqual(query);
    });

    it('should make host and page search (Full Search)', () => {
        let result;
        const searchParam = '//demo.dotcms.com/about-us';
        const query = {
            query: {
                query_string: {
                    query: `+basetype:5 +path:*about-us* +conhostName:demo.dotcms.com`
                }
            }
        };
        this.dotPageSelectorService.search(searchParam).subscribe(res => {
            result = res;
        });

        this.lastConnection.mockRespond(
            new Response(
                new ResponseOptions({
                    body: {
                        contentlets: [mockDotSiteSelectorResults.data[0].payload]
                    }
                })
            )
        );

        this.lastConnection.mockRespond(
            new Response(
                new ResponseOptions({
                    body: {
                        contentlets: [mockDotPageSelectorResults.data[0].payload]
                    }
                })
            )
        );

        expect(result).toEqual(mockDotPageSelectorResults);
        expect(this.lastConnection.request.url).toContain('es/search');
        expect(this.lastConnection.request._body).toEqual(query);
    });

    it('should get a page by identifier', () => {
        let result;
        const searchParam = 'fdeb07ff-6fc3-4237-91d9-728109bc621d';
        const query = {
            query: {
                query_string: {
                    query: `+basetype:5 +identifier:*${searchParam}*`
                }
            }
        };

        this.dotPageSelectorService.getPageById(searchParam).subscribe(res => {
            result = res;
        });

        this.lastConnection.mockRespond(
            new Response(
                new ResponseOptions({
                    body: {
                        contentlets: [mockDotPageSelectorResults.data[0].payload]
                    }
                })
            )
        );
        expect(result).toEqual(mockDotPageSelectorResults.data[0]);
        expect(this.lastConnection.request.url).toContain('es/search');
        expect(this.lastConnection.request.method).toEqual(1);
        expect(this.lastConnection.request._body).toEqual(query);
    });
});
