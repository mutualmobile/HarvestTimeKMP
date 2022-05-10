//
//  LaunchScreen.swift
//  macOS
//
//  Created by Anmol Verma on 10/05/22.
//

import Foundation
import SwiftUI

struct LaunchScreen : View{
    var body: some View{
        VStack(alignment:.center){
            Text("harvest").foregroundColor(Color.white).bold().font(.largeTitle)
            SignInWithGoogle()
            LoginForm().padding(EdgeInsets.init(top: 0, leading: 24, bottom: 0, trailing: 24))
        }
            .frame(width: 320 , height: 530, alignment: .top)
            .background(
                LinearGradient(gradient: Gradient(colors: [
                    Color("appColor"),
                    Color("appColorLight")]), startPoint: .leading, endPoint: .trailing)

                    .edgesIgnoringSafeArea(.all))
    }
}
