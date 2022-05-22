//
//  MainContent.swift
//  macOS
//
//  Created by Anmol Verma on 12/05/22.
//

import Foundation
import SwiftUI

struct MainContent : View{
    @Binding var date: Date
    var newEntry: (()->Void)
    var dateUpdate: ((Date)->Void)

    var body: some View {
        VStack{
            DayNavigateButtons(date: $date) { Date in
                self.date = Date
                dateUpdate(Date)
            }
            
            HStack{
                
                NewEntryButton(newEntry: newEntry).padding(12)
                VStack{
                    WeekView(date:$date) { newDate in
                        self.date = newDate
                    }
                    DayRecord(date:$date)
                }
            }
        }
       
        
    }
}
