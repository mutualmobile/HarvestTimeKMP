//
//  AppError.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 26/05/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import Foundation

struct AppError: LocalizedError {
    let title: String
    let message: String
    
    init(title: String = "Error", message: String) {
        self.title = title
        self.message = message
    }
    
    init(_ error: Error) {
        self.title = "Error"
        self.message = error.localizedDescription
    }
    
    var errorDescription: String? { message }
}

// TODO: Nasir - Must be moved to its separate file
enum Day: Identifiable, Equatable {
    typealias RawValue = Int
    
    var id: UUID { UUID() }
    
    case saturday(Double, Bool, Bool)
    case sunday(Double, Bool, Bool)
    case monday(Double, Bool, Bool)
    case tuesday(Double, Bool, Bool)
    case wednesday(Double, Bool, Bool)
    case thursday(Double, Bool, Bool)
    case friday(Double, Bool, Bool)
    
    var tuple: (name: String, hours: String) {
        switch self {
        case .saturday(let hours, _ , _):
            return ("S" , String(format: "%.2f", hours))
        case .sunday(let hours, _ , _):
            return ("S" , String(format: "%.2f", hours))
        case .monday(let hours, _ , _):
            return ("M" , String(format: "%.2f", hours))
        case .tuesday(let hours, _ , _):
            return ("T" , String(format: "%.2f", hours))
        case .wednesday(let hours, _ , _):
            return ("W" , String(format: "%.2f", hours))
        case .thursday(let hours, _ , _):
            return ("T" , String(format: "%.2f", hours))
        case .friday(let hours, _ , _):
            return ("F" , String(format: "%.2f", hours))
        }
    }
    
    mutating func setSelected(_ selected: Bool) {
        switch self {
            
        case .saturday(let hours, _ , let current):
            self = .saturday(hours, selected, current)
        case .sunday(let hours, _ , let current):
            self = .sunday(hours, selected, current)
        case .monday(let hours, _ , let current):
            self = .monday(hours, selected, current)
        case .tuesday(let hours, _ , let current):
            self = .tuesday(hours, selected, current)
        case .wednesday(let hours, _ , let current):
            self = .wednesday(hours, selected, current)
        case .thursday(let hours, _ , let current):
            self = .thursday(hours, selected, current)
        case .friday(let hours, _ , let current):
            self = .friday(hours, selected, current)
        }
    }
    
    mutating func setMatched(_ matched: Bool) {
        switch self {
        case .saturday(let hours, let selected , _):
            self = .saturday(hours, selected, matched)
        case .sunday(let hours, let selected , _):
            self = .sunday(hours, selected, matched)
        case .monday(let hours, let selected , _):
            self = .monday(hours, selected, matched)
        case .tuesday(let hours, let selected , _):
            self = .tuesday(hours, selected, matched)
        case .wednesday(let hours, let selected , _):
            self = .wednesday(hours, selected, matched)
        case .thursday(let hours, let selected , _):
            self = .thursday(hours, selected, matched)
        case .friday(let hours, let selected , _):
            self = .friday(hours, selected, matched)
        }
    }
    
    var isSelectedMatched: (selectedDay: Bool, matchedDay: Bool) {
        switch self {
            
        case .saturday(_, let selected, let matched):
            return (selected, matched)
        case .sunday(_, let selected, let matched):
            return (selected, matched)
        case .monday(_, let selected, let matched):
            return (selected, matched)
        case .tuesday(_, let selected, let matched):
            return (selected, matched)
        case .wednesday(_, let selected, let matched):
            return (selected, matched)
        case .thursday(_, let selected, let matched):
            return (selected, matched)
        case .friday(_, let selected, let matched):
            return (selected, matched)
        }
    }
    
    var index: Int {
        switch self {
        case .saturday(_, _, _):
            return 0
        case .sunday(_, _, _):
            return 1
        case .monday(_, _, _):
            return 2
        case .tuesday(_, _, _):
            return 3
        case .wednesday(_, _, _):
            return 4
        case .thursday(_, _, _):
            return 5
        case .friday(_, _, _):
            return 6
        }
    }
}
