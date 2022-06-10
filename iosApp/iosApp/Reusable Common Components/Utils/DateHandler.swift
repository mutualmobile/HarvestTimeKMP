//
//  DateHandler.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 08/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.

///  Inspired by Mutual Mobile's Great developer `Vaibhav Khatri` from his talk on `Property Wrapper`

import Foundation

@propertyWrapper
struct DateHandler {
    private var _date: Date
    @StringTrimmer var format: String
        
    init(wrappedValue: Date, format: String = "EEEE, dd MMM") {
        self._date = wrappedValue
        self.format = format
    }
    
    var wrappedValue: Date {
        get {
            _date
        } set {
            _date = newValue
        }
    }
    
    var projectedValue: String {
        let formatter = DateHandler._dateFormatter
        formatter.dateFormat = format
        return formatter.string(from: _date)
    }
    
    static private let _dateFormatter: DateFormatter = {
        let formatter = DateFormatter()
        return formatter
    }()
}
