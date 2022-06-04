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
    
    init(title: String, message: String) {
        self.title = title
        self.message = message
    }
    
    init(_ error: Error) {
        self.title = "Error"
        self.message = error.localizedDescription
    }
    
    var errorDescription: String? { message }
}
