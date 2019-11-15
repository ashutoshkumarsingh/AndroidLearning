package com.example.daggerexample;

import dagger.Component;

@Component(modules = {RefrigeratorModule.class})
interface AppComponent {

    void inject(MainActivity mainActivity);

}
