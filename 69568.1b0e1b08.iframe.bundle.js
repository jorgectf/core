"use strict";(self.webpackChunkdotcms_ui=self.webpackChunkdotcms_ui||[]).push([[69568],{"./node_modules/date-fns/locale/mk/_lib/match/index.js":(module,exports,__webpack_require__)=>{var _interopRequireDefault=__webpack_require__("./node_modules/date-fns/node_modules/@babel/runtime/helpers/interopRequireDefault.js").default;Object.defineProperty(exports,"__esModule",{value:!0}),exports.default=void 0;var _index=_interopRequireDefault(__webpack_require__("./node_modules/date-fns/locale/_lib/buildMatchFn/index.js")),_default={ordinalNumber:(0,_interopRequireDefault(__webpack_require__("./node_modules/date-fns/locale/_lib/buildMatchPatternFn/index.js")).default)({matchPattern:/^(\d+)(-?[врмт][и])?/i,parsePattern:/\d+/i,valueCallback:function valueCallback(value){return parseInt(value,10)}}),era:(0,_index.default)({matchPatterns:{narrow:/^((пр)?н\.?\s?е\.?)/i,abbreviated:/^((пр)?н\.?\s?е\.?)/i,wide:/^(пред нашата ера|нашата ера)/i},defaultMatchWidth:"wide",parsePatterns:{any:[/^п/i,/^н/i]},defaultParseWidth:"any"}),quarter:(0,_index.default)({matchPatterns:{narrow:/^[1234]/i,abbreviated:/^[1234](-?[врт]?и?)? кв.?/i,wide:/^[1234](-?[врт]?и?)? квартал/i},defaultMatchWidth:"wide",parsePatterns:{any:[/1/i,/2/i,/3/i,/4/i]},defaultParseWidth:"any",valueCallback:function valueCallback(index){return index+1}}),month:(0,_index.default)({matchPatterns:{abbreviated:/^(јан|фев|мар|апр|мај|јун|јул|авг|сеп|окт|ноем|дек)/i,wide:/^(јануари|февруари|март|април|мај|јуни|јули|август|септември|октомври|ноември|декември)/i},defaultMatchWidth:"wide",parsePatterns:{any:[/^ја/i,/^Ф/i,/^мар/i,/^ап/i,/^мај/i,/^јун/i,/^јул/i,/^ав/i,/^се/i,/^окт/i,/^но/i,/^де/i]},defaultParseWidth:"any"}),day:(0,_index.default)({matchPatterns:{narrow:/^[нпвсч]/i,short:/^(не|по|вт|ср|че|пе|са)/i,abbreviated:/^(нед|пон|вто|сре|чет|пет|саб)/i,wide:/^(недела|понеделник|вторник|среда|четврток|петок|сабота)/i},defaultMatchWidth:"wide",parsePatterns:{narrow:[/^н/i,/^п/i,/^в/i,/^с/i,/^ч/i,/^п/i,/^с/i],any:[/^н[ед]/i,/^п[он]/i,/^вт/i,/^ср/i,/^ч[ет]/i,/^п[ет]/i,/^с[аб]/i]},defaultParseWidth:"any"}),dayPeriod:(0,_index.default)({matchPatterns:{any:/^(претп|попл|полноќ|утро|пладне|вечер|ноќ)/i},defaultMatchWidth:"any",parsePatterns:{any:{am:/претпладне/i,pm:/попладне/i,midnight:/полноќ/i,noon:/напладне/i,morning:/наутро/i,afternoon:/попладне/i,evening:/навечер/i,night:/ноќе/i}},defaultParseWidth:"any"})};exports.default=_default,module.exports=exports.default}}]);
//# sourceMappingURL=69568.1b0e1b08.iframe.bundle.js.map