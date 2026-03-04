/**
 * 融云IM Web SDK接入示例
 * 功能：连接融云服务器并监听所有消息
 */

console.log('脚本开始加载');

// 确保RongIMLib已定义
if (typeof RongIMLib === 'undefined') {
    console.error('错误: RongIMLib 未定义，融云SDK可能未正确加载');
    alert('融云SDK加载失败，请检查网络连接或刷新页面重试');
}

// 当前聊天室ID
let currentChatroomId = null;

// 直接执行初始化函数，而不是等待DOMContentLoaded
function initApp() {
    console.log('初始化应用');
    console.log('RongIMLib对象:', RongIMLib);
    
    // 打印所有可用方法，帮助调试
    for (let key in RongIMLib) {
        console.log(`RongIMLib.${key}:`, typeof RongIMLib[key]);
    }
    
    // DOM元素
    const connectBtn = document.getElementById('connectBtn');
    const disconnectBtn = document.getElementById('disconnectBtn');
    const appkeyInput = document.getElementById('appkey');
    const tokenInput = document.getElementById('token');
    const connectionStatus = document.getElementById('connectionStatus');
    const messageContainer = document.getElementById('messageContainer');
    const messageTableBody = document.getElementById('messageTableBody');
    const clearMessagesBtn = document.getElementById('clearMessagesBtn');
    const messageFilterInput = document.getElementById('messageFilter');
    
    // 聊天室相关元素
    const joinChatroomBtn = document.getElementById('joinChatroomBtn');
    const quitChatroomBtn = document.getElementById('quitChatroomBtn');
    const chatroomIdInput = document.getElementById('chatroomId');
    const messageCountInput = document.getElementById('messageCount');
    const chatroomStatus = document.getElementById('chatroomStatus');
    
    // 检查DOM元素是否存在
    if (!connectBtn || !disconnectBtn || !appkeyInput || !tokenInput || !connectionStatus || !messageContainer || !messageTableBody) {
        console.error('错误: 页面DOM元素未找到');
        return;
    }
    
    if (!joinChatroomBtn || !quitChatroomBtn || !chatroomIdInput || !messageCountInput || !chatroomStatus) {
        console.error('错误: 聊天室DOM元素未找到');
        // 继续运行，允许其他功能可用
    }

    // 初始化按钮事件
    connectBtn.addEventListener('click', connectRongCloud);
    disconnectBtn.addEventListener('click', disconnectRongCloud);
    
    // 初始化聊天室按钮事件
    if (joinChatroomBtn && quitChatroomBtn) {
        joinChatroomBtn.addEventListener('click', joinChatroom);
        quitChatroomBtn.addEventListener('click', quitChatroom);
    }
    
    // 初始化清空消息按钮事件
    if (clearMessagesBtn) {
        clearMessagesBtn.addEventListener('click', clearMessages);
    }

    // 初始化消息过滤输入框事件
    if (messageFilterInput) {
        messageFilterInput.addEventListener('input', filterDisplayedMessages);
    }
    
    // 初始状态设置
    disconnectBtn.disabled = true;
    if (quitChatroomBtn) {
        quitChatroomBtn.disabled = true;
    }
    
    /**
     * 清空消息表格
     */
    function clearMessages() {
        if (messageTableBody) {
            messageTableBody.innerHTML = '';
            console.log('已清空消息表格');
        }
    }

    /**
     * 过滤已显示的消息
     */
    function filterDisplayedMessages() {
        if (!messageTableBody || !messageFilterInput) return;

        const filterText = messageFilterInput.value.trim().toLowerCase();
        const rows = messageTableBody.querySelectorAll('tr');

        rows.forEach(row => {
            if (filterText === '') {
                // 如果过滤文本为空，显示所有消息
                row.style.display = '';
            } else {
                // 获取消息内容（第4列）
                const contentCell = row.querySelector('td:nth-child(4)');
                if (contentCell) {
                    const messageContent = contentCell.textContent.toLowerCase();
                    if (messageContent.includes(filterText)) {
                        row.style.display = '';
                    } else {
                        row.style.display = 'none';
                    }
                }
            }
        });

        console.log(`消息过滤完成，过滤关键字: "${filterText}"`);
    }
    
    /**
     * 初始化融云IM SDK
     * @param {string} appKey 融云AppKey
     * @returns {Object} 融云IM客户端实例
     */
    function initRongCloudIM(appKey) {
        try {
            // 如果不传入appKey，从输入框获取，如果输入框也为空，使用默认值
            const realAppKey = appKey || appkeyInput.value.trim() || '4z3hlwrv4dqnt';

            console.log('初始化融云SDK，使用AppKey:', realAppKey);
            
            // 尝试直接使用全局对象
            if (typeof RongIMLib === 'undefined') {
                console.error('RongIMLib 未定义');
                return null;
            }
            
            // 初始化SDK
            let imClient = null;
            
            if (typeof RongIMLib.init === 'function') {
                console.log('尝试使用 RongIMLib.init 方法');
                imClient = RongIMLib.init({
                    appkey: realAppKey
                });
                console.log('RongIMLib.init 返回值:', imClient);
            } 
            
            // 方法4: 直接使用RongIMLib作为客户端
            if (!imClient) {
                console.log('使用RongIMLib作为客户端');
                imClient = RongIMLib;
            }
            
            // 设置消息监听
            setupMessageListener();
            
            return imClient;
        } catch (error) {
            console.error('初始化融云SDK失败:', error);
            updateStatus('初始化融云SDK失败: ' + error.message, 'error');
            return null;
        }
    }
    
    /**
     * 设置消息监听器
     */
    function setupMessageListener() {
        try {
            console.log('设置消息监听器');
            
            // 获取Events常量
            const Events = RongIMLib.Events;
            console.log('RongIMLib.Events:', Events);
            
            if (typeof Events === 'undefined') {
                console.error('RongIMLib.Events 未定义');
                return;
            }
            
            // 确认MESSAGES常量存在
            if (typeof Events.MESSAGES === 'undefined') {
                console.error('RongIMLib.Events.MESSAGES 未定义');
                console.log('尝试列出所有可用的Events:');
                for (let key in Events) {
                    console.log(`- ${key}: ${Events[key]}`);
                }
            }
            
            // 定义消息监听回调
            const messageListener = (evt) => {
                console.log('收到消息事件:', evt);
                
                if (evt && evt.messages && Array.isArray(evt.messages)) {
                    console.log(`收到 ${evt.messages.length} 条消息`);
                    evt.messages.forEach(message => {
                        console.log('处理消息:', message);
                        displayMessage(message);
                    });
                } else {
                    console.log('收到的消息事件格式不符合预期:', evt);
                }
            };
            
            // 添加消息监听器
            if (typeof RongIMLib.addEventListener === 'function') {
                console.log('添加消息监听器: RongIMLib.addEventListener(Events.MESSAGES, messageListener)');
                RongIMLib.addEventListener(Events.MESSAGES, messageListener);
            } else {
                console.error('RongIMLib.addEventListener 方法未定义');
            }
            
            // 添加连接状态监听器
            if (Events.CONNECTION_STATUS && typeof RongIMLib.addEventListener === 'function') {
                console.log('添加连接状态监听器');
                RongIMLib.addEventListener(Events.CONNECTION_STATUS, (evt) => {
                    console.log('连接状态变化:', evt);
                    handleConnectionStatus(evt.status);
                });
            } else {
                console.log('无法添加连接状态监听器，Events.CONNECTION_STATUS可能未定义');
            }
        } catch (error) {
            console.error('设置消息监听器失败:', error);
        }
    }
    
    /**
     * 处理连接状态变化
     * @param {number} status 连接状态码
     */
    function handleConnectionStatus(status) {
        console.log('连接状态变化:', status);
        
        // 手动判断常见的状态码
        let statusText = '未知状态: ' + status;
        let statusType = 'error';
        
        if (status === 0 || status === 'CONNECTED') {
            statusText = '已连接';
            statusType = 'status';
        } else if (status === 1 || status === 'CONNECTING') {
            statusText = '连接中...';
            statusType = 'status';
        } else if (status === 2 || status === 'DISCONNECTED') {
            statusText = '未连接';
            statusType = 'error';
        } else if (status === 3 || status === 'NETWORK_UNAVAILABLE') {
            statusText = '网络不可用';
            statusType = 'error';
        } else if (status === 6 || status === 'KICKED_OFFLINE_BY_OTHER_CLIENT') {
            statusText = '其他设备登录，当前设备被踢下线';
            statusType = 'error';
        }
        
        updateStatus(statusText, statusType);
    }
    
    /**
     * 处理接收到的消息
     * @param {Object} message 消息对象
     */
    function handleReceivedMessage(message) {
        console.log('收到消息:', message);
        displayMessage(message);
    }

    /**
     * 连接到融云服务器
     */
    function connectRongCloud() {
        const token = tokenInput.value.trim();
        const appkey = appkeyInput.value.trim();

        if (!token) {
            updateStatus('请输入有效的Token', 'error');
            return;
        }

        if (!appkey) {
            updateStatus('请输入有效的AppKey', 'error');
            return;
        }

        try {
            // 如果未初始化，先初始化
            if (!window.imClient) {
                window.imClient = initRongCloudIM(appkey);
                if (!window.imClient) {
                    updateStatus('IM客户端初始化失败', 'error');
                    return;
                }
            }
            
            updateStatus('连接中...', 'status');
            console.log('开始连接，使用token:', token);
            
            // 尝试连接
            const client = window.imClient;
            
            if (typeof client.connect === 'function') {
                console.log('尝试使用客户端实例的connect方法');
                
                try {
                    client.connect(token)
                    .then(function(user) {
                        console.log('连接成功，用户信息:', user);
                        updateStatus('连接成功，用户ID: ' + (user.data.userId || user), 'status');
                        connectBtn.disabled = true;
                        disconnectBtn.disabled = false;
                    })
                    .catch(function(error) {
                        console.error('连接失败:', error);
                        updateStatus('连接失败: ' + (error.code ? error.code + ' - ' + error.msg : error.message), 'error');
                    });
                } catch (e) {
                    console.error('连接方法执行失败:', e);
                    updateStatus('连接方法执行失败: ' + e.message, 'error');
                }
                return;
            }
            
        } catch (error) {
            console.error('连接过程发生异常:', error);
            updateStatus('连接过程发生异常: ' + error.message, 'error');
        }
    }

    /**
     * 断开融云连接
     */
    function disconnectRongCloud() {
        try {
            const client = window.imClient;
            
            // 如果有聊天室，先退出聊天室
            if (currentChatroomId) {
                quitChatroom();
            }
            
            if (client) {
                console.log('尝试断开连接');
                
                if (typeof client.disconnect === 'function') {
                    client.disconnect();
                    console.log('已调用disconnect方法');
                    updateStatus('已断开连接', 'error');
                    connectBtn.disabled = false;
                    disconnectBtn.disabled = true;
                    return;
                }
                
                updateStatus('无法找到断开连接的方法', 'error');
            }
        } catch (error) {
            console.error('断开连接时发生错误:', error);
            updateStatus('断开连接失败: ' + error.message, 'error');
        }
    }
    
    /**
     * 加入聊天室
     */
    function joinChatroom() {
        const chatRoomId = chatroomIdInput.value.trim();
        if (!chatRoomId) {
            updateChatroomStatus('请输入有效的聊天室ID', 'error');
            return;
        }
        
        // 检查是否已经连接到融云
        if (!window.imClient) {
            updateChatroomStatus('请先连接到融云服务器', 'error');
            return;
        }
        
        try {
            const count = parseInt(messageCountInput.value) || 50;
            
            // 检查joinExistChatRoom方法是否存在
            if (typeof RongIMLib.joinChatRoom !== 'function') {
                console.error('RongIMLib.joinExistChatRoom 方法不存在');
                updateChatroomStatus('加入聊天室方法不可用', 'error');
                return;
            }
            
            updateChatroomStatus('加入聊天室中...', 'status');
            console.log(`尝试加入聊天室: ${chatRoomId}, 获取历史消息数: ${count}`);
            
            RongIMLib.joinChatRoom(chatRoomId, {
                count: count
            }).then(res => {
                console.log('加入聊天室结果:', res);
                
                const { code, data } = res;
                if (code === RongIMLib.ErrorCode.SUCCESS) {
                    console.log('加入聊天室成功:', data);
                    currentChatroomId = chatRoomId;
                    updateChatroomStatus(`已加入聊天室: ${chatRoomId}`, 'status');
                    
                    // 更新按钮状态
                    joinChatroomBtn.disabled = true;
                    quitChatroomBtn.disabled = false;
                    
                    // 添加系统消息
                    addSystemMessage(`已成功加入聊天室 ${chatRoomId}，开始接收消息...`);
                } else {
                    console.error('加入聊天室失败，错误码:', code);
                    updateChatroomStatus(`加入聊天室失败，错误码: ${code}`, 'error');
                }
            }).catch(error => {
                console.error('加入聊天室异常:', error);
                updateChatroomStatus('加入聊天室异常: ' + error.message, 'error');
            });
        } catch (error) {
            console.error('加入聊天室过程中发生异常:', error);
            updateChatroomStatus('加入聊天室过程中发生异常: ' + error.message, 'error');
        }
    }
    
    /**
     * 退出聊天室
     */
    function quitChatroom() {
        if (!currentChatroomId) {
            updateChatroomStatus('未加入任何聊天室', 'error');
            return;
        }
        
        try {
            // 检查quitChatRoom方法是否存在
            if (typeof RongIMLib.quitChatRoom !== 'function') {
                console.error('RongIMLib.quitChatRoom 方法不存在');
                updateChatroomStatus('退出聊天室方法不可用', 'error');
                return;
            }
            
            updateChatroomStatus('退出聊天室中...', 'status');
            console.log(`尝试退出聊天室: ${currentChatroomId}`);
            
            RongIMLib.quitChatRoom(currentChatroomId)
            .then(res => {
                console.log('退出聊天室结果:', res);
                
                const { code } = res;
                if (code === RongIMLib.ErrorCode.SUCCESS) {
                    console.log('退出聊天室成功');
                    updateChatroomStatus('已退出聊天室', 'status');
                    
                    // 更新按钮状态
                    joinChatroomBtn.disabled = false;
                    quitChatroomBtn.disabled = true;
                    
                    // 添加系统消息
                    addSystemMessage(`已退出聊天室 ${currentChatroomId}`);
                    
                    // 清除当前聊天室ID
                    currentChatroomId = null;
                } else {
                    console.error('退出聊天室失败，错误码:', code);
                    updateChatroomStatus(`退出聊天室失败，错误码: ${code}`, 'error');
                }
            }).catch(error => {
                console.error('退出聊天室异常:', error);
                updateChatroomStatus('退出聊天室异常: ' + error.message, 'error');
            });
        } catch (error) {
            console.error('退出聊天室过程中发生异常:', error);
            updateChatroomStatus('退出聊天室过程中发生异常: ' + error.message, 'error');
        }
    }

    /**
     * 更新连接状态显示
     * @param {string} message 状态信息
     * @param {string} type 状态类型 (status/error)
     */
    function updateStatus(message, type) {
        connectionStatus.textContent = message;
        connectionStatus.className = type || '';
        console.log('连接状态更新:', message);
    }
    
    /**
     * 更新聊天室状态显示
     * @param {string} message 状态信息
     * @param {string} type 状态类型 (status/error)
     */
    function updateChatroomStatus(message, type) {
        if (chatroomStatus) {
            chatroomStatus.textContent = message;
            chatroomStatus.className = type || '';
        }
        console.log('聊天室状态更新:', message);
    }

    /**
     * 显示接收到的消息
     * @param {Object} message 消息对象
     */
    function displayMessage(message) {
        if (!messageTableBody) {
            console.error('消息表格主体元素未找到');
            return;
        }

        try {
            // 将整个消息打印到控制台，便于分析结构
            let jsonContent = JSON.stringify(message);
            console.log('消息完整内容:', jsonContent);

            // 检查消息过滤
            const filterText = messageFilterInput ? messageFilterInput.value.trim() : '';
            if (filterText && !jsonContent.toLowerCase().includes(filterText.toLowerCase())) {
                console.log('消息被过滤，不包含关键字:', filterText);
                return;
            }
            
            // 尝试提取各种可能的字段
            const sendTime = new Date((message.sentTime || message.sendTime || message.receivedTime || Date.now())).toLocaleString();
            const fromUserId = message.senderUserId || message.senderId || message.targetId || '未知发送者';
            const messageType = message.messageType || message.objectName || message.type || '未知类型';
            
            // 提取聊天室信息
            const conversationType = message.conversationType || message.type || '';
            const targetId = message.targetId || '';
            
            // 如果是聊天室消息，但不是当前聊天室的，可以选择不显示
            if (conversationType === 4 || conversationType === 'CHATROOM') {
                if (currentChatroomId && targetId !== currentChatroomId) {
                    console.log(`跳过非当前聊天室(${currentChatroomId})的消息, 来自聊天室: ${targetId}`);
                    return;
                }
            }
            
            // 创建表格行
            const row = document.createElement('tr');
            
            // 创建并添加时间单元格
            const timeCell = document.createElement('td');
            timeCell.textContent = sendTime;
            row.appendChild(timeCell);
            
            // 创建并添加发送者ID单元格
            const senderCell = document.createElement('td');
            senderCell.textContent = fromUserId;
            row.appendChild(senderCell);
            
            // 创建并添加消息类型单元格
            const typeCell = document.createElement('td');
            typeCell.textContent = messageType;
            row.appendChild(typeCell);
            
            // 创建并添加消息内容单元格
            const contentCell = document.createElement('td');
            
            // 创建一个内容容器，用于控制高度和省略
            const contentContainer = document.createElement('div');
            contentContainer.className = 'message-content';
            contentContainer.textContent = jsonContent;
            
            // 创建复制按钮
            const copyButton = document.createElement('button');
            copyButton.className = 'copy-btn';
            copyButton.textContent = '复制';
            copyButton.setAttribute('data-content', jsonContent);
            copyButton.onclick = function() {
                copyToClipboard(jsonContent);
            };
            
            // 将内容容器和复制按钮添加到单元格
            contentCell.appendChild(contentContainer);
            contentCell.appendChild(copyButton);
            row.appendChild(contentCell);
            
            // 将行添加到表格
            messageTableBody.appendChild(row);
            
            // 滚动到底部
            messageContainer.scrollTop = messageContainer.scrollHeight;
        } catch (e) {
            console.error('解析消息时出错:', e);
            // 如果解析出错，添加错误行
            const errorRow = document.createElement('tr');
            const errorCell = document.createElement('td');
            errorCell.colSpan = 4;
            errorCell.textContent = '解析消息时出错: ' + e.message;
            errorRow.appendChild(errorCell);
            messageTableBody.appendChild(errorRow);
        }
    }

    /**
     * 复制文本到剪贴板
     * @param {string} text 要复制的文本
     */
    function copyToClipboard(text) {
        // 创建临时文本区域
        const textArea = document.createElement('textarea');
        textArea.value = text;
        textArea.style.position = 'fixed';  // 避免滚动到底部
        textArea.style.left = '-999999px';
        textArea.style.top = '-999999px';
        document.body.appendChild(textArea);
        textArea.focus();
        textArea.select();
        
        try {
            // 执行复制命令
            const successful = document.execCommand('copy');
            if (successful) {
                showCopySuccess();
                console.log('文本已复制到剪贴板');
            } else {
                console.error('复制失败');
            }
        } catch (err) {
            console.error('复制过程中出错:', err);
        }
        
        // 移除临时文本区域
        document.body.removeChild(textArea);
    }
    
    /**
     * 显示复制成功提示
     */
    function showCopySuccess() {
        const successElement = document.getElementById('copySuccess');
        if (successElement) {
            successElement.style.display = 'block';
            setTimeout(() => {
                successElement.style.display = 'none';
            }, 2000);
        }
    }

    /**
     * 在加入聊天室成功后添加系统消息
     */
    function addSystemMessage(message) {
        if (!messageTableBody) return;
        
        const row = document.createElement('tr');
        
        // 时间单元格
        const timeCell = document.createElement('td');
        timeCell.textContent = new Date().toLocaleString();
        row.appendChild(timeCell);
        
        // 发送者单元格 - 系统
        const senderCell = document.createElement('td');
        senderCell.textContent = '系统';
        row.appendChild(senderCell);
        
        // 类型单元格 - 系统消息
        const typeCell = document.createElement('td');
        typeCell.textContent = '系统消息';
        row.appendChild(typeCell);
        
        // 内容单元格
        const contentCell = document.createElement('td');
        
        // 创建一个内容容器，用于控制高度和省略
        const contentContainer = document.createElement('div');
        contentContainer.className = 'message-content';
        contentContainer.textContent = message;
        
        // 创建复制按钮
        const copyButton = document.createElement('button');
        copyButton.className = 'copy-btn';
        copyButton.textContent = '复制';
        copyButton.setAttribute('data-content', message);
        copyButton.onclick = function() {
            copyToClipboard(message);
        };
        
        // 将内容容器和复制按钮添加到单元格
        contentCell.appendChild(contentContainer);
        contentCell.appendChild(copyButton);
        row.appendChild(contentCell);
        
        // 添加样式
        row.style.backgroundColor = '#f0f8ff';
        
        // 添加到表格
        messageTableBody.appendChild(row);
    }
}

// 确保在DOM和SDK都准备好后执行
// 如果DOM已经加载完成，直接执行初始化
if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initApp);
} else {
    // 如果DOM已经加载完成，直接执行初始化
    setTimeout(initApp, 500); // 延迟一点执行，确保DOM和SDK都准备好
}

console.log('脚本加载结束'); 
