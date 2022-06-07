//
//  LaunchView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 20/05/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import Foundation
import SwiftUI

struct LaunchView: View {
    
    @State private var selection = 0
    @State private var loginPresented = false
    
    private var textList = [
        "launch-text-1",
        "launch-text-2",
        "launch-text-3",
        "launch-text-4"
    ]
    
    var body: some View {
        VStack {
            Spacer().frame(height: UIScreen.main.bounds.height * 0.1)
            headerView
            pageView
            signinButton
            footerView
        }
        .frame(width: UIScreen.main.bounds.width,
               height: UIScreen.main.bounds.height,
               alignment: .center)
        .background(ColorAssets.colorBackground.color).edgesIgnoringSafeArea(.all)
    }
    
    var headerView: some View {
        VStack {
            Text("launch-title")
                .font(.system(size: 35,
                              weight: .bold,
                              design: .default))
            Text(LocalizedStringKey(textList[selection]))
                .multilineTextAlignment(.center)
                .padding(.horizontal)
        }
        .foregroundColor(ColorAssets.white.color)
    }
    
    var pageView: some View {
        TabView(selection: $selection) {
            Image("launch_1").tag(0)
            Image("launch_1").tag(1)
            Image("launch_1").tag(2)
            Image("launch_1").tag(3)
        }
        .tabViewStyle(PageTabViewStyle())
    }
    
    var signinButton: some View {
        Button {
            loginPresented = true
        } label: {
            Text("Sign In")
                .harvestButton()
        }
        .cornerRadius(15.0)
        .padding()
        .fullScreenCover(isPresented: $loginPresented) {
            LoginView()
        }
    }
    
    var footerView: some View {
        HStack {
            Text("Don't have an account?")
            Button {
                // TODO: (Nasir) Handle action
            } label: {
                Text("Try Harvest Free")
                    .font(.headline)
            }
        }
        .foregroundColor(ColorAssets.white.color)
        .padding(EdgeInsets(top: 0, leading: 0, bottom: 40, trailing: 0))
    }
}

#if DEBUG
struct LaunchView_Previews: PreviewProvider {
    static var previews: some View {
        LaunchView()
    }
}
#endif
