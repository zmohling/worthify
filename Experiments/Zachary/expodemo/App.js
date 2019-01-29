/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * @format
 * @flow
 */

import React, {Component} from 'react';
import {StyleSheet, Text, View, Button, Alert, ScrollView, Platform, ImageBackground, Dimensions, Image} from 'react-native';
import Swiper from 'react-native-swiper';

type Props = {};
export default class App extends Component<Props> {
  constructor(props) {
    super (props);

    // Get Window Dimensions
    const {height, width} = Dimensions.get('window');

    this.background = React.createRef();

    this.state = {
      width: width,
      height: height,
      backgroundImageWidth: 0,
      index: 1,
    };
    console.log(this.state.width + " : " + this.state.height)

    this.updateParallaxPositionState = this.updateParallaxPositionState.bind(this);
    this.handleLayout = this.handleLayout.bind(this);
  }

  scrollPosition = 0.5;

  _onPressButton() {
    Alert.alert('You tapped the button!')
  }

  /*
   *  Position of scroll. Could be used for parallax effect
   */
  updateParallaxPositionState(givenPosition) {
    if (givenPosition == undefined) { return; }
    this.scrollPosition = parseFloat(givenPosition)
    this.forceUpdate();

    console.log(this.scrollPosition);
  }

  getTranlate() {
    let value = ( ( this.scrollPosition ) * -1 ) * 125 - ( ( this.state.backgroundImageWidth - this.state.width ) / 2 );
    console.log(value);

    return value;
  }

  handleLayout(e) {
    this.setState(
        { backgroundImageWidth: e.nativeEvent.layout.width }
    );
  }

  render() {
    return (
        <ImageBackground
          ref={this.background}
          source={require('./assets/parallax-background.jpg')}
          style={{flex: 0, height: this.state.height, aspectRatio: 1.77}}
          onLayout={this.handleLayout}
          imageStyle={{
            transform: [
              {
                translateX: this.getTranlate(),
              }
            ],
            borderRadius: 20,
          }}
        >
          <View style={{width: this.state.width, height: this.state.height}}>
            <Swiper
              loop={false}
              dot = <View style={styles.dot} />
              activeDot = <View style={styles.activeDot} />
              index = {1}
              handleScroll = {this.updateParallaxPositionState}
            >
              <View style={styles.slide0}>
                <Text style={styles.whiteText} adjustFontSizeToFit={true}>
                  News Cards
                </Text>
              </View>
              <View style={styles.slide0}>
                <Text style={styles.blackText}>
                  Graph, Numerical Total, and Toggle List of Assets and Liabilities
                </Text>
                <View style={styles.buttonContainer}>
                  <Button
                    onPress={this._onPressButton}
                    title="Press Me"
                    />
                </View>
              </View>
              <View style={styles.slide0}>
                <Text style={styles.whiteText}>
                  Transactions
                </Text>
              </View>
            </Swiper>
          </View>
        </ImageBackground>
    );
  }
}

const styles = StyleSheet.create({
  wrapper: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: 'transparent'
  },
  slide0: {
    flex: 1,
    justifyContent: 'center',
    //paddingHorizontal: 20,
    alignItems: 'center',
    backgroundColor: 'transparent'
  },
  slide1: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#e6e6e6'
  },
  slide2: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#4d4d4d'
  },
  blackText: {
    //paddingHorizontal: 20,
    color: '#000000',
    fontSize: 50,
    //fontWeight: 'bold',
    paddingHorizontal: 20,
    textAlign: 'center',
    marginBottom: 5,
 },
 whiteText: {
   color: '#fff',
   fontSize: 50,
   //fontWeight: 'bold',
   paddingHorizontal: 20,
   textAlign: 'center',
   marginBottom: 5,
 },
 buttonContainer: {
   margin: 20
 },
 dot: {
   backgroundColor:'rgba(0,0,0,.2)',
   width: 8,
   height: 8,
   borderRadius: 4,
   marginLeft: 3,
   marginRight: 3,
   marginTop: 3,
   marginBottom: 3
 },
 activeDot: {
   backgroundColor:'rgba(0,0,0,.2)',
   width: 24,
   height: 8,
   borderRadius: 6,
   marginLeft: 5,
   marginRight: 5,
   marginTop: 3,
   marginBottom: 3
 }
});
