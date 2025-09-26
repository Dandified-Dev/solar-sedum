import User from "@/models/user";

const USER_ID = 1;
const USER_FIRST_NAME = "Dannièl"
const USER_LAST_NAME = "Herlaar";
const USER_EMAIL = "test";
const USER_ROLE = "user";
const USER_TEAM_ID = null;

let user1, user2;

beforeEach(function (){
    user1 = new User(USER_ID, USER_FIRST_NAME, USER_LAST_NAME, USER_EMAIL, USER_ROLE, USER_TEAM_ID);
    user2 = User.createSample(USER_ID+1)
});

it('creates a proper sample author with a valid lastName', function () {

    // Check if user2 and user2.lastName are defined
    expect(user2).toBeDefined();
    expect(user2.lastName).toBeDefined();

    // Check if lastName is not null
    expect(user2.lastName).not.toBeNull();

    // You can add additional assertions here based on your requirements
    // For example, you might check if the lastName is a string, etc.
});


it('clones a proper copy', function() {
    let copy = User.copyConstructor(user2)
    expect(copy !== user2);
})

it('constructs a proper author', function() {

    expect(user1).toBeDefined();
    expect(user1.id).toBeDefined();
    expect(user1.id).toBe(USER_ID);
    expect(user1.lastName).toBe(USER_LAST_NAME);
    expect(user1.firstName).toBe(USER_FIRST_NAME);
})
