"use strict";(self.webpackChunkdotcms_ui=self.webpackChunkdotcms_ui||[]).push([[3378],{"./node_modules/date-fns/_lib/isSameUTCWeek/index.js":(module,exports,__webpack_require__)=>{var _interopRequireDefault=__webpack_require__("./node_modules/date-fns/node_modules/@babel/runtime/helpers/interopRequireDefault.js").default;Object.defineProperty(exports,"__esModule",{value:!0}),exports.default=function isSameUTCWeek(dirtyDateLeft,dirtyDateRight,options){(0,_index.default)(2,arguments);var dateLeftStartOfWeek=(0,_index2.default)(dirtyDateLeft,options),dateRightStartOfWeek=(0,_index2.default)(dirtyDateRight,options);return dateLeftStartOfWeek.getTime()===dateRightStartOfWeek.getTime()};var _index=_interopRequireDefault(__webpack_require__("./node_modules/date-fns/_lib/requiredArgs/index.js")),_index2=_interopRequireDefault(__webpack_require__("./node_modules/date-fns/_lib/startOfUTCWeek/index.js"));module.exports=exports.default},"./node_modules/date-fns/locale/bg/_lib/formatRelative/index.js":(module,exports,__webpack_require__)=>{var _interopRequireDefault=__webpack_require__("./node_modules/date-fns/node_modules/@babel/runtime/helpers/interopRequireDefault.js").default;Object.defineProperty(exports,"__esModule",{value:!0}),exports.default=void 0;var _index=_interopRequireDefault(__webpack_require__("./node_modules/date-fns/toDate/index.js")),_index2=_interopRequireDefault(__webpack_require__("./node_modules/date-fns/_lib/isSameUTCWeek/index.js")),weekdays=["неделя","понеделник","вторник","сряда","четвъртък","петък","събота"];function thisWeek(day){var weekday=weekdays[day];return 2===day?"'във "+weekday+" в' p":"'в "+weekday+" в' p"}var formatRelativeLocale={lastWeek:function lastWeekFormatToken(dirtyDate,baseDate,options){var date=(0,_index.default)(dirtyDate),day=date.getUTCDay();return(0,_index2.default)(date,baseDate,options)?thisWeek(day):function lastWeek(day){var weekday=weekdays[day];switch(day){case 0:case 3:case 6:return"'миналата "+weekday+" в' p";case 1:case 2:case 4:case 5:return"'миналия "+weekday+" в' p"}}(day)},yesterday:"'вчера в' p",today:"'днес в' p",tomorrow:"'утре в' p",nextWeek:function nextWeekFormatToken(dirtyDate,baseDate,options){var date=(0,_index.default)(dirtyDate),day=date.getUTCDay();return(0,_index2.default)(date,baseDate,options)?thisWeek(day):function nextWeek(day){var weekday=weekdays[day];switch(day){case 0:case 3:case 6:return"'следващата "+weekday+" в' p";case 1:case 2:case 4:case 5:return"'следващия "+weekday+" в' p"}}(day)},other:"P"},_default=function formatRelative(token,date,baseDate,options){var format=formatRelativeLocale[token];return"function"==typeof format?format(date,baseDate,options):format};exports.default=_default,module.exports=exports.default}}]);
//# sourceMappingURL=3378.fcd61180.iframe.bundle.js.map