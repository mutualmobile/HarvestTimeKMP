//
//  macOSApp.swift
//  macOS
//
//  Created by Anmol Verma on 27/12/21.
//

import SwiftUI
import shared
import AppKit

@main
struct macOSApp: App {
    
    init() {
        KoinKt.doInitSharedDependencies()
    }
    
    var body: some Scene {
        WindowGroup {
            
            RootView()
   
        }.windowStyle(HiddenTitleBarWindowStyle())
    }
}
