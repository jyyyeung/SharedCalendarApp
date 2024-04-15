<?php

use App\Http\Controllers\Controller;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;

class AuthController extends Controller
{
    public function login(Request $request)
    {
        $request->validate([
            'email' => 'required|email',
            'password' => 'required',
            'device_name' => 'required'
        ]);
        // Find the first user with this email
        $user = User::where('email', $request->email)->first();

        // If the user does not exist or the password is incorrect
        if (!$user || !Hash::check($request->password, $user->password)) {
            return response(['status' => 404, 'message' => "Not Found!!!"]);
        }

        $token = $user->createToken($request->device_name)->plainTextToken;
        // Return the token
        return response([
            'token' => $token,
            'user' => $user,
            'status' => 200,
            'message' => 'Success'
        ]);
    }

    public function logout(Request $request)
    {
        // Revoke token used to authenticate the request
        $request->user()->currentAccessToken()->delete();
        return response([
            'status' => 200,
            'message' => 'Logged out'
        ]);
    }
}
