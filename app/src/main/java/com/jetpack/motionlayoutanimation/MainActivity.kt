package com.jetpack.motionlayoutanimation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionLayoutDebugFlags
import androidx.constraintlayout.compose.MotionScene
import androidx.constraintlayout.compose.layoutId
import com.jetpack.motionlayoutanimation.ui.theme.MotionLayoutAnimationTheme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MotionLayoutAnimationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        topBar = {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = "MotionLayout Animation",
                                        modifier = Modifier.fillMaxWidth(),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            )
                        },
                        content = {
                            MotionLayoutAnimation()
                        }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MotionLayoutAnimation() {
    var componentHeight by remember { mutableStateOf(1000f) }
    val swipeableState = rememberSwipeableState("Bottom")
    val anchors = mapOf(0f to "Bottom", componentHeight to "Top")

    val mprogress = (swipeableState.offset.value / componentHeight)

    MotionLayout(motionScene = MotionScene(
        """{
                ConstraintSets: {
                  start: {
                    Variables: {
                      texts: { tag: 'text' },
                      margin: { from: 0, step: 50 }
                    },
                    Generate: {
                      texts: {
                        top: ['parent', 'top', 'margin'],
                        start: ['parent', 'end', 16 ]
                      }
                    },
                    box: {
                      width: 'spread',
                      height: 64,
                      centerHorizontally: 'parent',
                      bottom: ['parent','bottom']
                    },
                    content: {
                      width: 'spread',
                      height: '400',
                      centerHorizontally: 'parent',
                      top: ['box','bottom', 32]
                    },
                    name: {
                      centerVertically: 'box',
                      start: ['parent', 'start', 16]
                    }
                  },
                  end: {
                    Variables: {
                      texts: { tag: 'text' },
                      margin: { from: 0, step: 50 }
                    },
                    Generate: {
                      texts: {
                        start: ['parent','start', 32],
                        top: ['content', 'top', 'margin']
                      }
                    },
                    box: {
                      width: 'spread',
                      height: 200,
                      centerHorizontally: 'parent',
                      top: ['parent','top']
                    },
                    content: {
                      width: 'spread',
                      height: 'spread',
                      centerHorizontally: 'parent',
                      top: ['box','bottom'],
                      bottom: ['parent', 'bottom']
                    },
                    name: {
                      rotationZ: 90,
                      scaleX: 2,
                      scaleY: 2,
                      end: ['parent', 'end', 16],
                      top: ['parent', 'top', 90]
                    }
                  }
                },
                Transitions: {
                  default: {
                    from: 'start',
                    to: 'end',
                    pathMotionArc: 'startHorizontal',
                    KeyFrames: {
                      KeyAttributes: [
                        {
                          target: ['box','content'],
                          frames: [50],
                          rotationZ: [25] 
                        }
                      ]
                    }
                  }
                }
            }"""
    ),
        progress = mprogress,
        debug = EnumSet.of(MotionLayoutDebugFlags.NONE),
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .swipeable(
                state = swipeableState,
                anchors = anchors,
                reverseDirection = true,
                thresholds = { _, _ -> FractionalThreshold(0.3f) },
                orientation = Orientation.Vertical
            )
            .onSizeChanged { size ->
                componentHeight = size.height.toFloat()
            }
    ) {
        Box(
            modifier = Modifier
                .layoutId("content")
                .background(Color.LightGray)
        )
        Box(
            modifier = Modifier
                .layoutId("box")
                .background(Color.Cyan)
        )
        Text(
            modifier = Modifier.layoutId("name"),
            text = "Compose"
        )
        for (i in 0 until 5) {
            Text(
                modifier = Modifier.layoutId("Make it Easy${i + 1}", "text"),
                text = "Make it Easy ${i + 1}"
            )
        }
    }
}



























