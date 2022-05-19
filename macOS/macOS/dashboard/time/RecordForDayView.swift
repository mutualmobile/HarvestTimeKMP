//
//  RecordForDayView.swift
//  macOS
//
//  Created by Anmol Verma on 12/05/22.
//

import Foundation
import SwiftUI

struct DayRecord: View {
    @Binding var date: Date

    var body: some View{
        VStack {
            
            Text("Random Quote \n ~ Someone").padding()
            
        }.background(.gray.opacity(0.6))
    }
}
