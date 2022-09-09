FROM antonkazakov/otus

RUN mkdir /opt/project
COPY .  /opt/project
WORKDIR /opt/project

ENV VERSION_SDK_TOOLS "8092744_latest"
ENV ANDROID_SDK_ROOT "/sdk"
ENV PATH "${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/cmdline-tools/tools/bin:${ANDROID_SDK_ROOT}/platform-tools:${ANDROID_SDK_ROOT}/emulator:${GRADLE_PROFILER}/bin"
ENV GRADLE_PROFILER "/gradle-profiler/gradle-profiler-0.18.0"

RUN mkdir -p ${ANDROID_SDK_ROOT}/cmdline-tools && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-${ANDROID_SDK_VERSION}_latest.zip && \
    unzip *tools*linux*.zip -d ${ANDROID_SDK_ROOT}/cmdline-tools && \
    mv ${ANDROID_SDK_ROOT}/cmdline-tools/cmdline-tools ${ANDROID_SDK_ROOT}/cmdline-tools/tools && \
    rm *tools*linux*.zip

COPY gradle-profiler-0.18.0.zip gradle-profiler/

RUN unzip gradle-profiler/gradle-profiler-0.18.0.zip -d gradle-profiler && rm -v gradle-profiler/gradle-profiler-0.18.0.zip

RUN yes | sdkmanager --licenses && \
		sdkmanager "platforms;android-32" "build-tools;30.0.2" && \
		cd /opt/project

USER jenkins