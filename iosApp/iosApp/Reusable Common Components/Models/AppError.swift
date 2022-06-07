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
enum Day: Identifiable {
    var id: UUID { UUID() }
    
    case saturday(Double)
    case sunday(Double)
    case monday(Double)
    case tuesday(Double)
    case wednesday(Double)
    case thursday(Double)
    case friday(Double)
    
    var logHours: (String, String) {
        switch self {
        case .saturday(let hours):
            return ("S" ,  String(format: "%.2f", hours))
        case .sunday(let hours):
            return ("S" ,  String(format: "%.2f", hours))
        case .monday(let hours):
            return ("M" ,  String(format: "%.2f", hours))
        case .tuesday(let hours):
            return ("T" ,  String(format: "%.2f", hours))
        case .wednesday(let hours):
            return ("W" ,  String(format: "%.2f", hours))
        case .thursday(let hours):
            return ("T" ,  String(format: "%.2f", hours))
        case .friday(let hours):
            return ("F" ,  String(format: "%.2f", hours))
        }
    }
}
