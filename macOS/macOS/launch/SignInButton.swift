//
//  SignInWithGoogle.swift
//  macOS
//
//  Created by Anmol Verma on 10/05/22.
//

import Foundation
import SwiftUI

struct SignInButton : View{
    
    var title:String
    var onClick: (() -> Void)

    var body: some View{
        VStack{
            Button {
                onClick()
            } label: {
                Text(title)
                    .foregroundColor(Color("appColor"))
                    .padding()
            }.buttonStyle(BorderedButtonStyle())
        }
    }
}
