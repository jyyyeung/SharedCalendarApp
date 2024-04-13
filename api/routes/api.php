<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\AccountController;
use App\Http\Controllers\Api\CalendarController;
use App\Http\Controllers\Api\EventController;
use App\Http\Controllers\Api\ShareController;

Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');


Route::apiResource('accounts', AccountController::class);
Route::apiResource('calendars', CalendarController::class);
Route::apiResource('events', EventController::class);
Route::apiResource('shares', ShareController::class);
