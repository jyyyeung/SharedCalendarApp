<?php

use App\Http\Controllers\Controller;
use App\Models\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Validator;

class AuthController extends Controller
{
    public function login(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'email' => 'required|email',
            'password' => 'required',
            'device_name' => 'required'
        ]);
        if ($validator->fails()) {
            return response()->json($validator->errors());
        }
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
            'message' => 'Logout successful'
        ]);
    }

    public function register(Request $request)
    {
        $request->validate([
            'username' => 'required',
            'email' => 'required|email|unique:users',
            'password' => 'required',
            'device_name' => 'required'
        ]);

        $user = User::create([
            'username' => $request->username,
            'email' => $request->email,
            'password' => Hash::make($request->password)
        ]);

        $token = $user->createToken($request->device_name)->plainTextToken;
        return response([
            'token' => $token,
            'user' => $user,
            'token_type' => 'Bearer',
            'status' => 201,
            'message' => 'User created successfully'
        ]);
    }

    public function me(Request $request)
    {
        return $request->user();
    }
}
