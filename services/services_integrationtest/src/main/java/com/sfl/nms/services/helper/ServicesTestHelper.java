package com.sfl.nms.services.helper;

import com.sfl.nms.services.device.UserDeviceService;
import com.sfl.nms.services.device.dto.UserDeviceDto;
import com.sfl.nms.services.device.model.UserDevice;
import com.sfl.nms.services.device.model.mobile.DeviceOperatingSystemType;
import com.sfl.nms.services.notification.UserNotificationService;
import com.sfl.nms.services.notification.dto.NotificationDto;
import com.sfl.nms.services.notification.dto.UserNotificationDto;
import com.sfl.nms.services.notification.dto.email.EmailNotificationDto;
import com.sfl.nms.services.notification.dto.push.*;
import com.sfl.nms.services.notification.dto.push.sns.PushNotificationSnsRecipientDto;
import com.sfl.nms.services.notification.dto.sms.SmsNotificationDto;
import com.sfl.nms.services.notification.email.EmailNotificationService;
import com.sfl.nms.services.notification.model.Notification;
import com.sfl.nms.services.notification.model.NotificationProviderType;
import com.sfl.nms.services.notification.model.NotificationState;
import com.sfl.nms.services.notification.model.UserNotification;
import com.sfl.nms.services.notification.model.email.EmailNotification;
import com.sfl.nms.services.notification.model.push.*;
import com.sfl.nms.services.notification.model.push.sns.PushNotificationSnsRecipient;
import com.sfl.nms.services.notification.model.sms.SmsNotification;
import com.sfl.nms.services.notification.push.PushNotificationService;
import com.sfl.nms.services.notification.push.PushNotificationSubscriptionProcessingService;
import com.sfl.nms.services.notification.push.PushNotificationSubscriptionRequestService;
import com.sfl.nms.services.notification.push.PushNotificationSubscriptionService;
import com.sfl.nms.services.notification.push.sns.PushNotificationSnsRecipientService;
import com.sfl.nms.services.notification.sms.SmsNotificationService;
import com.sfl.nms.services.user.UserService;
import com.sfl.nms.services.user.dto.UserDto;
import com.sfl.nms.services.user.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import static org.junit.Assert.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * User: Ruben Dilanyan
 * Company: SFL LLC
 * Date: 12/24/15
 * Time: 7:16 PM
 */
@Component
public class ServicesTestHelper {

    /* Constants */
    private static final int IOS_TOKEN_LENGTH = 64;

    private static final String HEX_CHARACTER_SET = "ABCDEF0123456789";

    private static final String YO_YO = "YoYo";

    private static final String EMAIL_FROM = "dummy_sender@dummy.com";

    private static final String EMAIL_TO = "dummy_recipient@dummy.com";

    private static final String IP_ADDRESS = "127.0.0.1";

    private static final String MOBILE_NUMBER = "+37455000000";

    private static final int PUSH_NOTIFICATION_PROPERTIES_COUNT = 10;

    public static final String APPLICATION_TYPE = "customer";

    /* Dependencies */
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserService userService;

    @Autowired
    private PushNotificationSubscriptionService pushNotificationSubscriptionService;

    @Autowired
    private PushNotificationSnsRecipientService pushNotificationSnsRecipientService;

    @Autowired
    private PushNotificationService pushNotificationService;

    @Autowired
    private PushNotificationSubscriptionRequestService pushNotificationSubscriptionRequestService;

    @Autowired
    private PushNotificationSubscriptionProcessingService pushNotificationSubscriptionProcessingService;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    private SmsNotificationService smsNotificationService;

    @Autowired
    private UserNotificationService userNotificationService;

    @Autowired
    private UserDeviceService userDeviceService;

    /* Constructors */
    public ServicesTestHelper() {
    }

    /* User */
    public UserDto createUserDto() {
        final UserDto userDto = new UserDto();
        userDto.setUuId(UUID.randomUUID().toString());
        return userDto;
    }

    public User createUser(final UserDto userDto) {
        return userService.createUser(userDto);
    }

    public User createUser() {
        return createUser(createUserDto());
    }

    public void assertUser(final User user, final UserDto userDto) {
        assertNotNull(user);
        assertNotNull(user.getId());
        assertEquals(userDto.getUuId(), user.getUuId());
    }

    /* User device */
    public UserDeviceDto createUserDeviceDto() {
        final UserDeviceDto userDeviceDto = new UserDeviceDto();
        userDeviceDto.setOsType(DeviceOperatingSystemType.ANDROID);
        userDeviceDto.setUuId(UUID.randomUUID().toString());
        return userDeviceDto;
    }

    public UserDevice createUserDevice() {
        return createUserDevice(createUser(), createUserDeviceDto());
    }

    public UserDevice createUserDevice(final User user, final UserDeviceDto userDeviceDto) {
        return userDeviceService.createUserDevice(user.getId(), userDeviceDto);
    }

    public void assertUserDevice(final UserDevice userDevice, final UserDeviceDto userDeviceDto) {
        assertNotNull(userDevice);
        assertNotNull(userDevice.getId());
        assertEquals(userDeviceDto.getUuId(), userDevice.getUuId());
        assertEquals(userDeviceDto.getOsType(), userDevice.getOsType());
    }

    /* Push notification subscription */
    public PushNotificationSubscriptionDto createPushNotificationSubscriptionDto() {
        return new PushNotificationSubscriptionDto();
    }

    public PushNotificationSubscription createPushNotificationSubscription(final User user, final PushNotificationSubscriptionDto subscriptionDto) {
        return pushNotificationSubscriptionService.createPushNotificationSubscription(user.getId(), subscriptionDto);
    }

    public PushNotificationSubscription createPushNotificationSubscription() {
        return createPushNotificationSubscription(createUser(), createPushNotificationSubscriptionDto());
    }

    public void assertPushNotificationSubscription(final PushNotificationSubscription subscription, final PushNotificationSubscriptionDto subscriptionDto) {
        assertNotNull(subscription);
        assertNotNull(subscription.getId());
    }

    /* Push notification recipient */
    public PushNotificationSnsRecipientDto createPushNotificationSnsRecipientDto() {
        final PushNotificationSnsRecipientDto recipientDto = new PushNotificationSnsRecipientDto();
        recipientDto.setDestinationRouteToken("JHYFTTDRSDESYRSESRDYESESESRTDESHDSSDD");
        recipientDto.setDeviceOperatingSystemType(DeviceOperatingSystemType.IOS);
        recipientDto.setApplicationType(APPLICATION_TYPE);
        recipientDto.setPlatformApplicationArn("arn:aws:sns:eu-west-1:6554779700788:app/GCM/prod_sfl_android");
        return recipientDto;
    }

    public PushNotificationSnsRecipient createPushNotificationSnsRecipient(final PushNotificationSubscription subscription, final PushNotificationSnsRecipientDto recipientDto) {
        return pushNotificationSnsRecipientService.createPushNotificationRecipient(subscription.getId(), recipientDto);
    }

    public PushNotificationSnsRecipient createPushNotificationSnsRecipient() {
        return createPushNotificationSnsRecipient(createPushNotificationSubscription(), createPushNotificationSnsRecipientDto());
    }

    public void assertPushNotificationSnsRecipient(final PushNotificationSnsRecipient recipient, final PushNotificationSnsRecipientDto recipientDto) {
        assertNotNull(recipient);
        assertEquals(recipient.getType(), recipientDto.getType());
        assertEquals(recipient.getDestinationRouteToken(), recipientDto.getDestinationRouteToken());
        assertEquals(recipient.getDeviceOperatingSystemType(), recipientDto.getDeviceOperatingSystemType());
        assertEquals(recipient.getApplicationType(), recipientDto.getApplicationType());
        assertEquals(recipient.getStatus(), PushNotificationRecipientStatus.ENABLED);
        assertEquals(recipient.getPlatformApplicationArn(), recipientDto.getPlatformApplicationArn());
    }

    public PushNotificationRecipient createPushNotificationRecipientForIOSDeviceAndRegisterWithAmazonSns(final User user, final String iOSDeviceToken) {
        // Create user mobile device
        final UserDeviceDto userMobileDeviceDto = createUserDeviceDto();
        userMobileDeviceDto.setOsType(DeviceOperatingSystemType.IOS);
        final UserDevice userMobileDevice = createUserDevice(user, userMobileDeviceDto);
        // Create token processing parameters
        final PushNotificationSubscriptionProcessingParameters parameters = new PushNotificationSubscriptionProcessingParameters();
        parameters.setUserId(user.getId());
        parameters.setDeviceToken(iOSDeviceToken);
        parameters.setSubscribe(true);
        parameters.setUserMobileDeviceId(userMobileDevice.getId());
        parameters.setCurrentPushNotificationProviderType(null);
        parameters.setCurrentProviderToken(null);
        parameters.setApplicationType(APPLICATION_TYPE);
        // Process subscription
        final PushNotificationRecipient recipient = pushNotificationSubscriptionProcessingService.processPushNotificationSubscriptionChange(parameters);
        flush();
        return recipient;
    }

    /* Push notification request request */
    public PushNotificationSubscriptionRequestDto createPushNotificationSubscriptionRequestDto() {
        final PushNotificationSubscriptionRequestDto subscriptionDto = new PushNotificationSubscriptionRequestDto();
        subscriptionDto.setSubscribe(true);
        subscriptionDto.setUserDeviceToken("KULGYIFTIDTDIR^R(CJFDRFGRTD^%586durtX");
        subscriptionDto.setPreviousSubscriptionRequestUuId("*Y&)^$%$&#&%%&^(^$%*TLGFTRTCRDR");
        subscriptionDto.setApplicationType(APPLICATION_TYPE);
        return subscriptionDto;
    }

    public PushNotificationSubscriptionRequest createPushNotificationSubscriptionRequest(final User user, final UserDevice userMobileDevice, final PushNotificationSubscriptionRequestDto requestDto) {
        return pushNotificationSubscriptionRequestService.createPushNotificationSubscriptionRequest(user.getId(), userMobileDevice.getId(), requestDto);
    }

    public PushNotificationSubscriptionRequest createPushNotificationSubscriptionRequest() {
        final User user = createUser();
        final UserDevice userMobileDevice = createUserDevice(user, createUserDeviceDto());
        return createPushNotificationSubscriptionRequest(user, userMobileDevice, createPushNotificationSubscriptionRequestDto());
    }

    public void assertPushNotificationSubscriptionRequest(final PushNotificationSubscriptionRequest request, final PushNotificationSubscriptionRequestDto requestDto) {
        assertNotNull(request);
        assertNotNull(request.getId());
        assertEquals(requestDto.getUserDeviceToken(), request.getUserDeviceToken());
        assertEquals(requestDto.isSubscribe(), request.isSubscribe());
        assertEquals(requestDto.getPreviousSubscriptionRequestUuId(), request.getPreviousSubscriptionRequestUuId());
        assertEquals(requestDto.getApplicationType(), request.getApplicationType());
        assertEquals(PushNotificationSubscriptionRequestState.CREATED, request.getState());
    }

    /* Email notification */
    public EmailNotificationDto createEmailNotificationDto() {
        final EmailNotificationDto notificationDto = new EmailNotificationDto();
        notificationDto.setRecipientEmail(EMAIL_TO);
        notificationDto.setSenderEmail(EMAIL_FROM);
        notificationDto.setClientIpAddress(IP_ADDRESS);
        notificationDto.setContent(YO_YO + YO_YO);
        notificationDto.setSubject(YO_YO);
        notificationDto.setProviderType(NotificationProviderType.SMTP_SERVER);
        return notificationDto;
    }

    public EmailNotification createEmailNotification() {
        return createEmailNotification(createEmailNotificationDto());
    }

    public EmailNotification createEmailNotification(final EmailNotificationDto notificationDto) {
        return emailNotificationService.createEmailNotification(notificationDto);
    }

    public void assertEmailNotification(final EmailNotification notification, final EmailNotificationDto notificationDto) {
        assertNotification(notification, notificationDto);
        assertEquals(notificationDto.getRecipientEmail(), notification.getRecipientEmail());
        assertEquals(notificationDto.getSenderEmail(), notification.getSenderEmail());
        assertEquals(notificationDto.getProviderType(), notification.getProviderType());
    }

    /* SMS notification */
    public SmsNotificationDto createSmsNotificationDto() {
        final SmsNotificationDto notificationDto = new SmsNotificationDto();
        notificationDto.setRecipientMobileNumber(MOBILE_NUMBER);
        notificationDto.setClientIpAddress(IP_ADDRESS);
        notificationDto.setContent(YO_YO + YO_YO);
        notificationDto.setSubject(YO_YO);
        notificationDto.setProviderType(NotificationProviderType.TWILLIO);
        return notificationDto;
    }

    public SmsNotification createSmsNotification() {
        return createSmsNotification(createSmsNotificationDto());
    }

    public SmsNotification createSmsNotification(final SmsNotificationDto notificationDto) {
        return smsNotificationService.createSmsNotification(notificationDto);
    }

    public void assertSmsNotification(final SmsNotification notification, final SmsNotificationDto notificationDto) {
        assertNotification(notification, notificationDto);
        assertEquals(notificationDto.getRecipientMobileNumber(), notification.getRecipientMobileNumber());
        Assert.assertEquals(notificationDto.getProviderType(), notification.getProviderType());
    }

    /* Push notification */
    public PushNotificationDto createPushNotificationDto() {
        final PushNotificationDto notificationDto = new PushNotificationDto();
        notificationDto.setClientIpAddress(IP_ADDRESS);
        notificationDto.setContent(YO_YO + YO_YO);
        notificationDto.setSubject(YO_YO);
        return notificationDto;
    }

    public PushNotification createPushNotification() {
        return createPushNotification(createPushNotificationSnsRecipient(), createPushNotificationDto(), createPushNotificationPropertyDTOs(PUSH_NOTIFICATION_PROPERTIES_COUNT));
    }

    public PushNotification createPushNotification(final PushNotificationRecipient recipient, final PushNotificationDto notificationDto, final List<PushNotificationPropertyDto> pushNotificationPropertyDTos) {
        return pushNotificationService.createNotification(recipient.getId(), notificationDto, pushNotificationPropertyDTos);
    }

    public void assertPushNotification(final PushNotification notification, final PushNotificationDto notificationDto) {
        assertNotification(notification, notificationDto);
    }

    public void assertNotification(final Notification notification, final NotificationDto<? extends Notification> notificationDto) {
        assertNotNull(notification);
        assertNotNull(notification.getId());
        assertEquals(notificationDto.getClientIpAddress(), notification.getClientIpAddress());
        assertEquals(notificationDto.getContent(), notification.getContent());
        assertEquals(notificationDto.getSubject(), notification.getSubject());
        assertEquals(notificationDto.getType(), notification.getType());
        Assert.assertEquals(NotificationState.CREATED, notification.getState());
    }

    /* Push notifications properties */
    public PushNotificationPropertyDto createPushNotificationPropertyDto() {
        final PushNotificationPropertyDto pushNotificationPropertyDto = new PushNotificationPropertyDto();
        pushNotificationPropertyDto.setPropertyKey("testPropertyKey");
        pushNotificationPropertyDto.setPropertyValue("testPropertyValue");
        return pushNotificationPropertyDto;
    }

    public PushNotificationProperty createPushNotificationProperty(final PushNotificationPropertyDto notificationPropertyDto) {
        final PushNotificationProperty pushNotificationProperty = new PushNotificationProperty();
        notificationPropertyDto.updateDomainEntityProperties(pushNotificationProperty);
        return pushNotificationProperty;
    }

    public void assertPushNotificationProperty(final PushNotificationProperty pushNotificationProperty, final PushNotificationPropertyDto pushNotificationPropertyDto) {
        assertEquals(pushNotificationPropertyDto.getPropertyKey(), pushNotificationProperty.getPropertyKey());
        assertEquals(pushNotificationPropertyDto.getPropertyValue(), pushNotificationProperty.getPropertyValue());
    }

    public List<PushNotificationPropertyDto> createPushNotificationPropertyDTOs(final int count) {
        final List<PushNotificationPropertyDto> pushNotificationPropertyDTOs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            final PushNotificationPropertyDto pushNotificationPropertyDto = createPushNotificationPropertyDto();
            pushNotificationPropertyDto.setPropertyKey(pushNotificationPropertyDto.getPropertyKey() + "_" + i);
            pushNotificationPropertyDto.setPropertyValue(pushNotificationPropertyDto.getPropertyValue() + "_" + i);
            // Add to the list of notifications
            pushNotificationPropertyDTOs.add(pushNotificationPropertyDto);
        }
        return pushNotificationPropertyDTOs;
    }

    /* User notification */
    public UserNotificationDto createUserNotificationDto() {
        return new UserNotificationDto();
    }

    public UserNotification createUserNotification() {
        return createUserNotification(createUserNotificationDto());
    }

    public UserNotification createUserNotification(final UserNotificationDto notificationDto) {
        final User user = createUser();
        final Notification notification = createSmsNotification();
        return userNotificationService.createUserNotification(user.getId(), notification.getId(), notificationDto);
    }

    public void assertUserNotification(final UserNotification notification, final UserNotificationDto notificationDto) {
        assertNotNull(notification);
    }

    /* Device tokens */
    public String generateIOSToken() {
        return RandomStringUtils.random(IOS_TOKEN_LENGTH, HEX_CHARACTER_SET);
    }

    /* Utility methods */
    private void flush() {
        entityManager.flush();
    }

    private void clear() {
        entityManager.clear();
    }

    private void flushAndClear() {
        flush();
        clear();
    }
}
