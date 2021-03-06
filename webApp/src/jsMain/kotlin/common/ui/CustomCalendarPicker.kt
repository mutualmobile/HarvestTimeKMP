@file:JsModule("@mui/x-date-pickers/CalendarPicker")
@file:JsNonModule

package common.ui

import muix.pickers.CalendarPickerClasses
import muix.pickers.CalendarPickerView
import muix.pickers.PickerOnChangeFn
import kotlin.js.Date

import csstype.ClassName
import kotlinx.js.ReadonlyArray

external interface CalendarPickerProps : react.PropsWithClassName {
    override var className: ClassName?

    var classes: CalendarPickerClasses?

    /**
     * The components used for each slot.
     * Either a string to use an HTML element or a component.
     * @default {}
     */
    var components: dynamic

    /**
     * The props used for each slot inside.
     * @default {}
     */
    var componentsProps: CalendarPickerSlotsComponentsProps?

    var date: Date?

    /**
     * Default calendar month displayed when `value={null}`.
     */
    var defaultCalendarMonth: Date?

    /**
     * If `true`, the picker and text field are disabled.
     * @default false
     */
    var disabled: Boolean?

    /**
     * @default false
     */
    var disableFuture: Boolean?

    /**
     * @default false
     */
    var disablePast: Boolean?

    /**
     * Max selectable date. @DateIOType
     */
    var maxDate: Date?

    /**
     * Min selectable date. @DateIOType
     */
    var minDate: Date?

    /**
     * Callback fired on view change.
     * @param {CalendarPickerView} view The new view.
     */
    var onViewChange: ((view: CalendarPickerView) -> Unit)?

    /**
     * Callback fired on date change
     */
    var onChange: PickerOnChangeFn<Date>

    /**
     * Initially open view.
     * @default 'day'
     */
    var openTo: CalendarPickerView?

    /**
     * Make picker read only.
     * @default false
     */
    var readOnly: Boolean?

    /**
     * Disable heavy animations.
     * @default typeof navigator !== 'undefined' && /(android)/i.test(navigator.userAgent)
     */
    var reduceAnimations: Boolean?

    /**
     * Component displaying when passed `loading` true.
     * @returns {React.ReactNode} The node to render when loading.
     * @default () => <span data-mui-test="loading-progress">...</span>
     */
    var renderLoading: (() -> react.ReactNode)?

    /**
     * Disable specific date. @DateIOType
     * @template Date
     * @param {Date} day The date to check.
     * @returns {boolean} If `true` the day will be disabled.
     */
    var shouldDisableDate: ((day: Date) -> Boolean)?

    /**
     * Controlled open view.
     */
    var view: CalendarPickerView?

    /**
     * Views for calendar picker.
     * @default ['year', 'day']
     */
    var views: ReadonlyArray<CalendarPickerView>?
}

external interface CalendarPickerSlotsComponent

external interface CalendarPickerSlotsComponentsProps : react.Props

/**
 *
 * Demos:
 *
 * - [Date Picker](https://mui.com/x/react-date-pickers/date-picker/)
 *
 * API:
 *
 * - [CalendarPicker API](https://mui.com/x/api/date-pickers/calendar-picker/)
 */
external val CalendarPicker: react.FC<CalendarPickerProps>
