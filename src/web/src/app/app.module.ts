import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

import { AppComponent } from './app.component';
import { VersionsListModule } from './versions-list/versions-list.module';
import { AppRoutingModule } from './app-routing.module';
import { WholeVersionModule } from './whole-version/whole-version.module';
import { Spinner } from './spinner/spinner.component'

@NgModule({
  declarations: [
    AppComponent,
    Spinner,
  ],
  imports: [
    VersionsListModule,
    WholeVersionModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NoopAnimationsModule
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
