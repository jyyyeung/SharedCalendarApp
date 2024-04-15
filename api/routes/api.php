<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\AccountController;
use App\Http\Controllers\Api\CalendarController;
use App\Http\Controllers\Api\EventController;
use App\Http\Controllers\Api\ShareController;
use App\Models\User;
use Illuminate\Support\Facades\Hash;

Route::post('/login', [AuthController::class, 'login']);

// Requires Authorization Bearer {{ token }} in Headers
Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');

Route::middleware(['auth:sanctum'])->group(
    function () { {
            Route::post('/logout', [AuthController::class, 'logout']);
        }
    }
);

Route::apiResource('accounts', AccountController::class);
Route::apiResource('calendars', CalendarController::class);
Route::apiResource('events', EventController::class);
Route::apiResource('shares', ShareController::class);
