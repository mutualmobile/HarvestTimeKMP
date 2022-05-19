//
//  LoginForm.swift
//  macOS
//
//  Created by Anmol Verma on 10/05/22.
//

import Foundation
import SwiftUI

struct LoginForm : View{
    
    
    @State var workEmail:String = ""
    @State var password:String = ""
    
    var body: some View{
        VStack{
            TextField("", text: $workEmail)
                .placeholder("Work Email",when: workEmail.isEmpty)
                .textFieldStyle(PlainTextFieldStyle()).padding(EdgeInsets.init(top: 4, leading: 4, bottom: 4, trailing: 4))
                .frame( minHeight: 30, alignment: .center)
                .background(Color.white.opacity(0.2))
                .foregroundColor(Color.white)
                .cornerRadius(5)
            TextField("", text: $password)
                .placeholder("Password",when: password.isEmpty)
                .textFieldStyle(PlainTextFieldStyle()).padding(EdgeInsets.init(top: 4, leading: 4, bottom: 4, trailing: 4))
                .frame( minHeight: 30, alignment: .center)
                .background(Color.white.opacity(0.2))
                .foregroundColor(Color.white)
                .cornerRadius(5)

        }
    }
}

extension View {
    func placeholder(
        _ text: String,
        when shouldShow: Bool,
        alignment: Alignment = .leading) -> some View {

            placeholder(when: shouldShow, alignment: alignment) { Text(text).foregroundColor(.white.opacity(0.7)) }
    }
    
    func placeholder<Content: View>(
         when shouldShow: Bool,
         alignment: Alignment = .leading,
         @ViewBuilder placeholder: () -> Content) -> some View {

         ZStack(alignment: alignment) {
             placeholder().opacity(shouldShow ? 1 : 0)
             self
         }
     }
}
