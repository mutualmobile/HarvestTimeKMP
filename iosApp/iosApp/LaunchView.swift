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
    
    private var textList = [
        "Simple, powerful time tracking, reporting and invoicing",
        "Track time easily, wherever you are",
        "Enter expanses on go",
        "Report on time for powerful insights"
    ]
    
    var body: some View {
        
        GeometryReader { proxy in
            VStack {
                Spacer().frame(height: 80.0)
                headerView
                pageView
                signinButton(with: proxy)
                    .padding()
                footerView
                    .padding(.bottom)
            }
            .frame(width: proxy.size.width, height: proxy.size.height, alignment: .center)
            .foregroundColor(ColorAssets.white.color)
            .background(ColorAssets.colorBackground.color.edgesIgnoringSafeArea(.all))
        }
        
    }
    
    var headerView: some View {
        VStack {
            Text("H A R V E S T")
                .font(.system(size: 35, weight: .bold, design: .default))
            Text(textList[selection])
                .multilineTextAlignment(.center)
                .padding(.horizontal)
        }
    }
    
    func signinButton(with proxy: GeometryProxy) -> some View {
        Button {
            
        } label: {
            Text("Sign In")
                .foregroundColor(.black)
                .font(.title3)
                .padding()
                .frame(width: proxy.size.width - 30, alignment: .center)
                .background(ColorAssets.white.color)
            
        }
        .cornerRadius(18.0)
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
    
    var footerView: some View {
        HStack {
            Text("Don't have an account?")
            Button {
                
            } label: {
                Text("Try Harvest Free")
                    .font(.headline)
            }
            
        }
    }
}

#if DEBUG
struct LaunchView_Previews: PreviewProvider {
    static var previews: some View {
        LaunchView()
        
    }
}
#endif
