"use strict";(self.webpackChunkdotcms_ui=self.webpackChunkdotcms_ui||[]).push([[74415],{"./node_modules/date-fns/locale/nl-BE/_lib/match/index.js":(module,exports,__webpack_require__)=>{var _interopRequireDefault=__webpack_require__("./node_modules/date-fns/node_modules/@babel/runtime/helpers/interopRequireDefault.js").default;Object.defineProperty(exports,"__esModule",{value:!0}),exports.default=void 0;var _index=_interopRequireDefault(__webpack_require__("./node_modules/date-fns/locale/_lib/buildMatchFn/index.js")),_default={ordinalNumber:(0,_interopRequireDefault(__webpack_require__("./node_modules/date-fns/locale/_lib/buildMatchPatternFn/index.js")).default)({matchPattern:/^(\d+)e?/i,parsePattern:/\d+/i,valueCallback:function valueCallback(value){return parseInt(value,10)}}),era:(0,_index.default)({matchPatterns:{narrow:/^([vn]\.? ?C\.?)/,abbreviated:/^([vn]\. ?Chr\.?)/,wide:/^((voor|na) Christus)/},defaultMatchWidth:"wide",parsePatterns:{any:[/^v/,/^n/]},defaultParseWidth:"any"}),quarter:(0,_index.default)({matchPatterns:{narrow:/^[1234]/i,abbreviated:/^K[1234]/i,wide:/^[1234]e kwartaal/i},defaultMatchWidth:"wide",parsePatterns:{any:[/1/i,/2/i,/3/i,/4/i]},defaultParseWidth:"any",valueCallback:function valueCallback(index){return index+1}}),month:(0,_index.default)({matchPatterns:{narrow:/^[jfmasond]/i,abbreviated:/^(jan.|feb.|mrt.|apr.|mei|jun.|jul.|aug.|sep.|okt.|nov.|dec.)/i,wide:/^(januari|februari|maart|april|mei|juni|juli|augustus|september|oktober|november|december)/i},defaultMatchWidth:"wide",parsePatterns:{narrow:[/^j/i,/^f/i,/^m/i,/^a/i,/^m/i,/^j/i,/^j/i,/^a/i,/^s/i,/^o/i,/^n/i,/^d/i],any:[/^jan/i,/^feb/i,/^m(r|a)/i,/^apr/i,/^mei/i,/^jun/i,/^jul/i,/^aug/i,/^sep/i,/^okt/i,/^nov/i,/^dec/i]},defaultParseWidth:"any"}),day:(0,_index.default)({matchPatterns:{narrow:/^[zmdwv]/i,short:/^(zo|ma|di|wo|do|vr|za)/i,abbreviated:/^(zon|maa|din|woe|don|vri|zat)/i,wide:/^(zondag|maandag|dinsdag|woensdag|donderdag|vrijdag|zaterdag)/i},defaultMatchWidth:"wide",parsePatterns:{narrow:[/^z/i,/^m/i,/^d/i,/^w/i,/^d/i,/^v/i,/^z/i],any:[/^zo/i,/^ma/i,/^di/i,/^wo/i,/^do/i,/^vr/i,/^za/i]},defaultParseWidth:"any"}),dayPeriod:(0,_index.default)({matchPatterns:{any:/^(am|pm|middernacht|het middaguur|'s (ochtends|middags|avonds|nachts))/i},defaultMatchWidth:"any",parsePatterns:{any:{am:/^am/i,pm:/^pm/i,midnight:/^middernacht/i,noon:/^het middaguur/i,morning:/ochtend/i,afternoon:/middag/i,evening:/avond/i,night:/nacht/i}},defaultParseWidth:"any"})};exports.default=_default,module.exports=exports.default}}]);
//# sourceMappingURL=74415.5f4410b3.iframe.bundle.js.map