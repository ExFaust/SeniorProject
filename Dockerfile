FROM antonkazakov/otus

ENV VERSION_SDK_TOOLS "8092744"
ENV ANDROID_SDK_ROOT "/sdk"
ENV GRADLE_PROFILER "/gradle-profiler/gradle-profiler-0.18.0"
ENV PATH "${PATH}:${ANDROID_SDK_ROOT}/cmdline-tools/latest/bin:${ANDROID_SDK_ROOT}/cmdline-tools/tools/bin:${ANDROID_SDK_ROOT}/platform-tools:${ANDROID_SDK_ROOT}/emulator:${GRADLE_PROFILER}/bin"

RUN mkdir -p ${ANDROID_SDK_ROOT}/cmdline-tools && \
    wget https://dl.google.com/android/repository/commandlinetools-linux-${VERSION_SDK_TOOLS}_latest.zip && \
    unzip *tools*linux*.zip -d ${ANDROID_SDK_ROOT}/cmdline-tools && \
    mv ${ANDROID_SDK_ROOT}/cmdline-tools/cmdline-tools ${ANDROID_SDK_ROOT}/cmdline-tools/tools && \
    rm *tools*linux*.zip

COPY gradle-profiler-0.18.0.zip gradle-profiler/

RUN unzip gradle-profiler/gradle-profiler-0.18.0.zip -d gradle-profiler && rm -v gradle-profiler/gradle-profiler-0.18.0.zip

RUN yes | sdkmanager --licenses

FROM jenkins/jenkins:2.361.1-jdk11

USER root

RUN apt-get update && apt-get install -y lsb-release
RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc \
  https://download.docker.com/linux/debian/gpg
RUN echo "deb [arch=$(dpkg --print-architecture) \
  signed-by=/usr/share/keyrings/docker-archive-keyring.asc] \
  https://download.docker.com/linux/debian \
  $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list
RUN apt-get update && apt-get install -y docker-ce-cli

USER jenkins