import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { VersionsListModule } from './versions-list/versions-list.module';
import { AppRoutingModule } from './app-routing.module';

import { AppComponent } from './app.component';
import { WholeVersionModule } from './whole-version/whole-version.module';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';


@NgModule({
  declarations: [
    AppComponent,
  ],
  imports: [
    VersionsListModule,
    WholeVersionModule,
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,
    NoopAnimationsModule,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
