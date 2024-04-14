<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;
use App\Http\Controllers\Api\AccountController;
use App\Http\Controllers\Api\CalendarController;
use App\Http\Controllers\Api\EventController;
use App\Http\Controllers\Api\ShareController;
use App\Models\User;
use Illuminate\Support\Facades\Hash;

// Requires Authorization Bearer {{ token }} in Headers
Route::get('/user', function (Request $request) {
    return $request->user();
})->middleware('auth:sanctum');


Route::apiResource('accounts', AccountController::class);
Route::apiResource('calendars', CalendarController::class);
Route::apiResource('events', EventController::class);
Route::apiResource('shares', ShareController::class);

Route::post('/login', function (Request $request) {
    $user = User::where('email', $request->email)->first();
    if (!$user || !Hash::check($request->password, $user->password)) {
        return response(['status' => 404, 'message' => "Not Found!!!"]);
    }
    return response(['token' => $user->createToken('token')->plainTextToken, 'status' => '200', 'message' => 'Success']);
});

// Route::middleware('auth:sanctum')->get('/user', function (Request $request) {
//     return $request->user();
// });
