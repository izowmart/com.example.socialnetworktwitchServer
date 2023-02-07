package com.example.data.repository.follow

import com.example.data.models.Following
import com.example.data.models.User
import org.litote.kmongo.and
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.insertOne
import org.litote.kmongo.eq
import org.litote.kmongo.inc

class FollowRepositoryImpl(
    db: CoroutineDatabase
) : FollowRepository {

    private val following = db.getCollection<Following>()
    private val users = db.getCollection<User>()

    override suspend fun followUserIfExists(followingUserId: String, followedUserId: String): Boolean {
        val doesFollowingUserExist = users.findOneById(followingUserId) != null
        val doesFollowedUserExist = users.findOneById(followedUserId) != null
        if (!doesFollowingUserExist || !doesFollowedUserExist) {
            return false

        }
        users.updateOneById(
            followingUserId,
            inc(User::followingUserId, 1)
        )
        users.updateOneById(
            followedUserId,
            inc(
                User::folloerCount,
                1
            )
        )
        following.insertOne(Following(followingUserId, followedUserId))
        return true
    }

    override suspend fun unfollowUserIfExists(followingUserId: String, followedUserId: String): Boolean {
        val deletedResult = following.deleteOne(
            and(
                Following::followingUserId eq followingUserId,
                Following::followedUserId eq followedUserId
            )
        )

        if(deletedResult.deletedCount > 0){
            users.updateOneById(
                followingUserId,
                inc(User::followingCount, -1)
            )
            users.updateOneById(
                followedUserId,
                inc(User::followedCount, -1)
            )
        }
        return deletedResult.deletedCount > 0
    }

    override suspend fun getFollowsByUser(userId: String): List<Following> {
        return following.find(Following::followingUserId eq userId).toList()
    }

    override suspend fun doesUserFollow(followingUserId: String, followedUserId: String): Boolean {
        return following.findOne(
            and(
                Following::followingUserId eq followingUserId,
                Following::followedUserId eq followedUserId
            )
        ) != null
    }
}