import {Injector, Provider} from 'angular2/core';
import {DataStore} from '../../../api/persistence/DataStore'
import {RestDataStore} from '../../../api/persistence/RestDataStore';
import {UserModel} from '../../../api/auth/UserModel';
import {ApiRoot} from '../../../api/persistence/ApiRoot';
import {I18nService} from "./I18n";
import {HTTP_PROVIDERS} from "angular2/http";


var injector = Injector.resolveAndCreate([
  UserModel,
  ApiRoot,
  I18nService,
  HTTP_PROVIDERS,
  new Provider(DataStore, {useClass: RestDataStore})
])

describe('Integration.api.system.locale.I18n', function () {

  var rsrcService:I18nService

  beforeAll(function () {
    rsrcService = injector.get(I18nService)
  })

  beforeEach(function () {
  });


  it("Can get a specific message.", function (done) {
    console.log("Called - 01", "can get specific")
    rsrcService.getForLocale('en-US', 'message.comment.success', true).subscribe((rsrc)=> {
      console.log("Called - 02", "can get specific")
      expect(rsrc).toBe("Your comment has been saved")
      rsrcService.getForLocale('de', 'message.comment.success', true).subscribe((rsrc)=> {
        expect(rsrc).toBe("Ihr Kommentar wurde gespeichert")
        done()
      })
    })
  })

  it("Can get all message under a particular path.", function (done) {
    let base = 'message.comment'
    rsrcService.getForLocale('en-US', base, false).subscribe((rsrc)=> {
      rsrcService.get(base + '.delete').subscribe((v)=> {
        expect(v).toBe("Your comment has been delete")
        rsrcService.get(base + '.failure').subscribe((v)=> {
          expect(v).toBe("Your comment couldn't be created")
          rsrcService.get(base + '.success').subscribe((v)=> {
            expect(v).toBe("Your comment has been saved")
            done()
          })
        })
      })
    })
  })

  it("Can get all message under a particular path in a non-default language.", function (done) {
    let base = 'message.comment'
    rsrcService.getForLocale('de', base, false).subscribe((rsrc)=> {
      rsrcService.getForLocale('de', base + '.delete').subscribe((v)=> {
        expect(v).toBe("Ihr Kommentar wurde gelöscht")
        rsrcService.getForLocale('de', base + '.failure').subscribe((v)=> {
          expect(v).toBe("Ihr Kommentar konnte nicht erstellt werden")
          rsrcService.getForLocale('de', base + '.success').subscribe((v)=> {
            expect(v).toBe("Ihr Kommentar wurde gespeichert")
            done()
          })
        })
      })
    })
  })


});
