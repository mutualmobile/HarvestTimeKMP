//
//  StringTrimmer.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 08/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.

///  Inspired by Mutual Mobile's Great developer `Vaibhav Khatri` from his talk on `Property Wrapper`

import Foundation

@propertyWrapper
struct StringTrimmer {
    private var _value: String
    var wrappedValue: String {
        get {
            _value.trimmingCharacters(in: .whitespacesAndNewlines)
        } set {
            _value = newValue
        }
    }
    
    init(wrappedValue: String) {
        _value = wrappedValue
    }
}
