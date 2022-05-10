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
            HStack{
                Text("harvest").foregroundColor(Color.white).bold().font(.largeTitle)
            }
        }
            .frame(width: 300 , height: 550, alignment: .center)
            .background(
                LinearGradient(gradient: Gradient(colors: [
                    Color("appColor"),
                    Color("appColorLight")]), startPoint: .leading, endPoint: .trailing)

                    .edgesIgnoringSafeArea(.all))
    }
}
