import {mount} from '@vue/test-utils';
import LoginComponent from '@/components/login/LoginComponent.vue';
import DashboardComponent from "@/components/dashboard/DashboardComponent.vue";
import {createRouter, createWebHistory} from 'vue-router';

const router = createRouter({
    history: createWebHistory(),
    routes: [
        {path: '/', component: DashboardComponent},
        {path: '/dashboard', component: DashboardComponent},
    ],
});

// Mock router object with additional jest.fn() for testing navigation
const mockRouter = {
    ...router,
    push: jest.fn(),   // Mock the router's push method
    currentRoute: {path: '/'},  // Set a default currentRoute path for testing
};

// Setup function to create a wrapper for the LoginComponent with mock dependencies
const setupWrapper = (authenticateMock = jest.fn().mockResolvedValue({
    success: true,
    token: 'fakeToken',
    userId: 1,
    email: 'test@example.com',
    role: 'admin',
})) => {
    return mount(LoginComponent, {
        global: {
            provide: {
                usersService: {
                    authenticate: authenticateMock, // Mocked authenticate method
                },
            },
            plugins: [mockRouter],  // Inject the mockRouter as a plugin
        },
    });
};

// Describe block for the LoginComponent tests
describe('LoginComponent', () => {
    let wrapper;

    beforeEach(() => {
        wrapper = setupWrapper();
    });

    afterEach(() => {
        wrapper.unmount();
    });


    // Test 1: Checks if the component renders correctly
    it('renders correctly', () => {
        expect(wrapper.exists()).toBe(true);
    });


    // Test 2: Simulates a successful login and checks if it navigates to /dashboard
    it('performs login successfully and navigates to /dashboard', async () => {
        // Arrange: Set email and password
        await wrapper.setData({
            email: 'test@example.com',
            password: 'password',
        });

        // Act: Trigger the login method
        await wrapper.vm.login();
        await wrapper.vm.$nextTick();

        // Assert: Check if router push was called with '/dashboard'
        expect(mockRouter.push).toHaveBeenCalledWith('/dashboard');
    });


    // Test 3: Simulates a failed login with invalid credentials
    it('handles login failure with invalid credentials', async () => {
        // Mock the authenticate function of usersService to simulate a failed login
        const authenticateMock = jest.fn().mockResolvedValue({success: false, message: 'Invalid credentials'});

        // Initialize the wrapper with the customized authenticateMock
        wrapper = setupWrapper(authenticateMock);

        // Simulate entering invalid login credentials and check if the error message is displayed
        await wrapper.setData({
            email: 'invalid@example.com',
            password: 'invalidpassword',
        });
        await wrapper.find('form').trigger('submit.prevent');
        await wrapper.vm.$nextTick();

        // Assertions to check if the error message is displayed correctly
        expect(authenticateMock).toHaveBeenCalledWith('invalid@example.com', 'invalidpassword');
        expect(wrapper.vm.error).toBe(true);
        expect(wrapper.find('.error-message').text()).toContain('Invalid credentials');
    });


    // Test 4: Simulates a failed login with empty fields
    it('handles login failure with empty fields', async () => {
        // Arrange: Leave email and password empty

        // Act: Trigger the login method with empty email and password
        await wrapper.vm.login();
        await wrapper.vm.$nextTick();

        // Assert: Check if the error message is displayed for empty fields
        expect(wrapper.vm.error).toBe(true);
        expect(wrapper.find('.error-message').text()).toContain("Email or password can't be empty.");
    });


    // Test 5: Toggles password visibility on button click
    it('toggles password visibility on button click', async () => {
        // Arrange: Password is initially hidden

        // Act: Trigger a click on the show-password-button
        await wrapper.find('.show-password-button').trigger('click');
        await wrapper.vm.$nextTick();

        // Assert: Check if the passwordFieldType is set to 'text'
        expect(wrapper.vm.passwordFieldType).toBe('text');
    });


    // Test 6: Simulates a successful login with correct credentials
    it('performs login successfully with correct credentials', async () => {
        // Mock the authenticate function to simulate a successful login
        const authenticateMock = jest.fn().mockResolvedValue({
            success: true,
            token: 'fakeToken',
            userId: 1,
            email: 'test@example.com',
            role: 'admin',
        });

        // Initialize the wrapper with the customized authenticateMock
        wrapper = setupWrapper(authenticateMock);

        // Simulate entering valid login credentials and check for no errors
        await wrapper.setData({
            email: 'valid@example.com',
            password: 'validpassword',
        });
        await wrapper.find('form').trigger('submit.prevent');
        await wrapper.vm.$nextTick();

        // Assertions to check if no errors occurred
        expect(authenticateMock).toHaveBeenCalledWith('valid@example.com', 'validpassword');
        expect(wrapper.vm.error).toBe(false);
        expect(wrapper.find('.error-message').exists()).toBe(false);
    });


});
