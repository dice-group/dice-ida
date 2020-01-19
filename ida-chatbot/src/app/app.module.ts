import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';

import { AppComponent } from './app.component';
import { MessageListComponent } from './components/message-list/message-list.component';
import { MessageItemComponent } from './components/message-item/message-item.component';
import { MessageFormComponent } from './components/message-form/message-form.component';
import { ChatboxComponent } from './components/chatbox/chatbox.component';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';
import {CdkTableModule} from '@angular/cdk/table';
import {HTTP_INTERCEPTORS, HttpClientModule} from '@angular/common/http';
import { FlexLayoutModule } from '@angular/flex-layout';
import {
  MatAutocompleteModule,
  MatButtonModule,
  MatButtonToggleModule,
  MatCardModule,
  MatCheckboxModule,
  MatChipsModule,
  MatDatepickerModule,
  MatDialogModule,
  MatDividerModule,
  MatExpansionModule,
  MatGridListModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatMenuModule,
  MatNativeDateModule,
  MatPaginatorModule,
  MatProgressBarModule,
  MatProgressSpinnerModule,
  MatRadioModule,
  MatRippleModule,
  MatSelectModule,
  MatSidenavModule,
  MatSliderModule,
  MatSlideToggleModule,
  MatSnackBarModule,
  MatSortModule,
  MatStepperModule,
  MatTableModule,
  MatTabsModule,
  MatToolbarModule,
  MatTooltipModule,
} from '@angular/material';
import {platformBrowserDynamic} from '@angular/platform-browser-dynamic';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import { SidebarComponent } from './components/sidebar/sidebar.component';
import { DataViewContainerComponent } from './components/data-view-container/data-view-container.component';
import { DatatableViewComponent } from './components/datatable-view/datatable-view.component';
import { FdgViewComponent } from './components/fdg-view/fdg-view.component';
import { BargraphViewComponent } from './components/bargraph-view/bargraph-view.component';
import { IntroComponent } from './components/intro/intro.component';
import { ClickStopPropagationDirective } from './directive/click-stop-propagation.directive';
import { TabViewComponent } from './components/tab-view/tab-view.component';
import {RestService} from './service/rest/rest.service';
import {DatatableDetailComponent} from './components/datatable-detail/datatable-detail.component';
import { SsbViewComponent } from './components/ssb-view/ssb-view.component';
import { VennViewComponent } from './components/venn-view/venn-view.component';
import { DeckglHexViewComponent } from './components/deckgl-hex-view/deckgl-hex-view.component';
import { SpeechInputComponent } from './components/speech-input/speech-input.component';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import { SoldierTimelineComponent } from './components/soldier-timeline/soldier-timeline.component'
@NgModule({
  declarations: [
    AppComponent,
    MessageListComponent,
    MessageItemComponent,
    MessageFormComponent,
    ChatboxComponent,
    SidebarComponent,
    DataViewContainerComponent,
    DatatableViewComponent,
    FdgViewComponent,
    BargraphViewComponent,
    IntroComponent,
    ClickStopPropagationDirective,
    TabViewComponent,
    DatatableDetailComponent,
    SsbViewComponent,
    VennViewComponent,
    DeckglHexViewComponent,
    SpeechInputComponent,
    SoldierTimelineComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    FormsModule,
    ReactiveFormsModule,
    HttpModule,
    HttpClientModule,
    CdkTableModule,
    MatAutocompleteModule,
    MatButtonModule,
    MatButtonToggleModule,
    MatCardModule,
    MatCheckboxModule,
    MatChipsModule,
    MatStepperModule,
    MatDatepickerModule,
    MatDialogModule,
    MatDividerModule,
    MatExpansionModule,
    MatGridListModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatMenuModule,
    MatNativeDateModule,
    MatPaginatorModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
    MatRadioModule,
    MatRippleModule,
    MatSelectModule,
    MatSidenavModule,
    MatSliderModule,
    MatSlideToggleModule,
    MatSnackBarModule,
    MatSortModule,
    MatTableModule,
    MatTabsModule,
    MatToolbarModule,
    MatTooltipModule,
    FlexLayoutModule,
    NgbModule
  ],
  providers: [{
    provide: HTTP_INTERCEPTORS,
    useClass: RestService ,
    multi: true
  }],
  bootstrap: [AppComponent]
})
export class AppModule { }
