//
//  SignupView.swift
//  iosApp
//
//  Created by Nasir Ahmed Momin on 01/06/22.
//  Copyright © 2022 Mutual Mobile. All rights reserved.
//

import SwiftUI

struct SignupView: View {
    
    @Environment(\.dismiss) var dismiss
    
    var body: some View {
        NavigationView {

            SwiftUI.Form {
                Text("Hello, World!")
            }
            
            .navigationTitle("Sign Up")
        }
        
    }
}

struct SignupView_Previews: PreviewProvider {
    static var previews: some View {
        SignupView()
    }
}
