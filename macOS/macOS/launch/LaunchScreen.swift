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
            Text("harvest").foregroundColor(Color.white).bold().font(.largeTitle).padding(EdgeInsets.init(top: 68, leading: 0, bottom: 0, trailing: 0))
            SignInButton(title:"Sign In with Google").padding(EdgeInsets.init(top: 24, leading: 0, bottom: 0, trailing: 0))
            LabelledDivider(label: "or",color: Color.white.opacity(0.7))
            LoginForm().padding(EdgeInsets.init(top: 0, leading: 24, bottom: 0, trailing: 24))
            SignInButton(title:"Sign In").padding(EdgeInsets.init(top: 12, leading: 0, bottom: 0, trailing: 0))
            Spacer()
            NoAccountLearnMore().padding()
        }
            .frame(width: 320 , height: 530, alignment: .top)
            .background(
                LinearGradient(gradient: Gradient(colors: [
                    Color("appColor"),
                    Color("appColorLight")]), startPoint: .leading, endPoint: .trailing)

                    .edgesIgnoringSafeArea(.all))
    }
}



struct NoAccountLearnMore: View{
    var body: some View{
        HStack(spacing: 4){
            Text("Don't have an account?").foregroundColor(Color.white.opacity(0.6))
            Text("Learn More").bold().foregroundColor(Color.white.opacity(0.8))
            
        }
    }
}

struct LabelledDivider: View {

    let label: String
    let horizontalPadding: CGFloat
    let color: Color

    init(label: String, horizontalPadding: CGFloat = 20, color: Color = .gray) {
        self.label = label
        self.horizontalPadding = horizontalPadding
        self.color = color
    }

    var body: some View {
        HStack {
            line
            Text(label).foregroundColor(color)
            line
        }
    }

    var line: some View {
        VStack { Divider().background(color) }.padding(horizontalPadding)
    }
}
