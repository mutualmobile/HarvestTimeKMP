//
//  RootView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 07/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI

class RootStore: ObservableObject {
    @Published var isAuthenticateUser = false
}

struct RootView: View {
    
    @StateObject var rootStore = RootStore()
    
    var body: some View {
        if rootStore.isAuthenticateUser {
            DashboardView()
        } else {
            LaunchView()
                .environmentObject(rootStore)
        }
    }
}
