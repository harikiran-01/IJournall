package com.bliss.auth.repo

class UserMockDataSource : UserDataSource {
    override suspend fun insertUser(user: User): Long {
        return 0
    }

    override suspend fun deleteUser(user: User) {
    }

    override suspend fun getUserbyName(userName: String): Result<User> {
        return Result.success(User())
    }

    override suspend fun getUserbyId(uid: Long): Result<User> {
        return Result.success(User())
    }
}