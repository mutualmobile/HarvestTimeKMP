//
//  SignInWithGoogle.swift
//  macOS
//
//  Created by Anmol Verma on 10/05/22.
//

import Foundation
import SwiftUI

struct SignInWithGoogle : View{
    var body: some View{
        Button {
            
        } label: {
            Text("Sign In with Google")
                .foregroundColor(Color("colorAccent"))
                .font(.title)
                .shadow(radius: 8)
        }.frame(minHeight: 40)
    }
}
