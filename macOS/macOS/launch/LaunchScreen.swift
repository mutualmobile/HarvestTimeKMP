//
//  LaunchScreen.swift
//  macOS
//
//  Created by Anmol Verma on 10/05/22.
//

import Foundation
import SwiftUI

struct RootView : View{
    
    
    @State var isDashboardViewActive = false
    
    
    var body: some View {
        
        if isDashboardViewActive == true {
            DashboardScreen()
        }else{
            LaunchScreen {
                isDashboardViewActive = true
            }
        }
        
        
    }
}

struct LaunchScreen : View{
    
    var onSignIn: (() -> Void)

    
    var body: some View{
        VStack(alignment:.center){
            Text("harvest").foregroundColor(Color.white).bold().font(.largeTitle).padding(EdgeInsets.init(top: 68, leading: 0, bottom: 0, trailing: 0))
            SignInButton(title:"Sign In with Google",onClick: {
                
            }).padding(EdgeInsets.init(top: 24, leading: 0, bottom: 0, trailing: 0))
            LabelledDivider(label: "or",color: Color.white.opacity(0.7))
            LoginForm().padding(EdgeInsets.init(top: 0, leading: 24, bottom: 0, trailing: 24))
            
           
          
            Spacer()
            NoAccountLearnMore().padding()
            
            GearButton()
            
        }
            .frame(width: 320 , height: 530, alignment: .top)
            .background(
                LinearGradient(gradient: Gradient(colors: [
                    Color("appColor"),
                    Color("appColorLight")]), startPoint: .leading, endPoint: .trailing)

                    .edgesIgnoringSafeArea(.all))
    }
}


struct GearButton : View{
    var body: some View{
        VStack(alignment: .trailing){
            
            Button {
                
            } label: {
                Image(systemName: "gearshape").resizable().frame(width: 24, height: 24, alignment: .bottomTrailing).foregroundColor(Color.white.opacity(0.7))
            }.buttonStyle(PlainButtonStyle())

        }.padding().frame(
            minWidth: 0,
            maxWidth: .infinity,
            alignment: .bottomTrailing
          )
    }
}

struct NoAccountLearnMore: View{
    var body: some View{
        HStack(spacing: 4){
            Text("Don't have an account?").foregroundColor(Color.white.opacity(0.6))
            Text("Learn More").foregroundColor(Color.white)
            
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
